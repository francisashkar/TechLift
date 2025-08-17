package com.example.techlift.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.techlift.PostsActivity
import com.example.techlift.R
import com.example.techlift.data.MockDataProvider
import com.example.techlift.model.User
import com.example.techlift.ui.adapter.ProfileInfoWindowAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class CommunityFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var shareLocationButton: Button
    private lateinit var viewPostsButton: Button
    private lateinit var zoomInButton: ImageButton
    private lateinit var zoomOutButton: ImageButton
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val markers = mutableListOf<Marker>()
    private lateinit var infoWindowAdapter: ProfileInfoWindowAdapter
    
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val DEFAULT_ZOOM = 15f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupMap()
        setupClickListeners()
    }

    private fun initializeViews(view: View) {
        shareLocationButton = view.findViewById(R.id.shareLocationButton)
        viewPostsButton = view.findViewById(R.id.viewPostsButton)
        zoomInButton = view.findViewById(R.id.zoomInButton)
        zoomOutButton = view.findViewById(R.id.zoomOutButton)
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun setupClickListeners() {
        shareLocationButton.setOnClickListener {
            if (checkLocationPermission()) {
                shareCurrentLocation()
            } else {
                requestLocationPermission()
            }
        }

        viewPostsButton.setOnClickListener {
            // Navigate to posts view showing all community posts
            val intent = Intent(requireContext(), PostsActivity::class.java)
            startActivity(intent)
        }
        
        // Setup zoom buttons
        zoomInButton.setOnClickListener {
            if (::map.isInitialized) {
                map.animateCamera(CameraUpdateFactory.zoomIn())
            }
        }
        
        zoomOutButton.setOnClickListener {
            if (::map.isInitialized) {
                map.animateCamera(CameraUpdateFactory.zoomOut())
            }
        }
    }

    private fun setupMapListeners() {
        map.setOnInfoWindowClickListener(this)
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Replaced with ActivityResultLauncher approach above

    private fun shareCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val geoPoint = GeoPoint(it.latitude, it.longitude)
                        val postData = hashMapOf(
                            "userId" to currentUser.uid,
                            "userName" to (currentUser.displayName ?: "משתמש"),
                            "location" to geoPoint,
                            "timestamp" to com.google.firebase.Timestamp.now(),
                            "type" to "location_share"
                        )

                        firestore.collection("community_posts")
                            .add(postData)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(requireContext(), "המיקום שותף בהצלחה! 📍", Toast.LENGTH_SHORT).show()
                                
                                // Add marker to map
                                val latLng = LatLng(it.latitude, it.longitude)
                                val marker = map.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title("${currentUser.displayName ?: "משתמש"}")
                                        .snippet("שיתף מיקום")
                                )
                                marker?.tag = documentReference.id
                                if (marker != null) {
                                    markers.add(marker)
                                }
                                
                                // Move camera to current location
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
                            }
                            .addOnFailureListener { _ ->
                                Toast.makeText(requireContext(), "שגיאה בשיתוף המיקום", Toast.LENGTH_SHORT).show()
                            }
                    }
                } ?: run {
                    Toast.makeText(requireContext(), "לא ניתן לקבל את המיקום הנוכחי", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadMockUsers() {
        // Clear existing markers
        markers.forEach { it.remove() }
        markers.clear()
        
        // Find the current user
        val currentUser = MockDataProvider.mockUsers.find { it.uid == "current_user" }
        val currentUserLocation = MockDataProvider.mockUserLocations["current_user"]
        
        // Add mock users to the map
        MockDataProvider.mockUsers.forEach { user ->
            val location = MockDataProvider.mockUserLocations[user.uid]
            if (location != null) {
                createUserMarker(user, location)
            }
        }
        
        // Center the map on the current user if available
        if (currentUserLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, 10f))
        } else {
            // Fallback to first user
            val firstLocation = MockDataProvider.mockUserLocations.values.firstOrNull()
            if (firstLocation != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
            }
        }
    }
    
    private fun createUserMarker(user: User, location: LatLng) {
        val isCurrentUser = user.uid == "current_user"
        
        if (user.photoUrl.isNotEmpty()) {
            // Load profile image and create a custom marker
            Glide.with(this)
                .asBitmap()
                .load(user.photoUrl)
                .circleCrop()
                .into(object : CustomTarget<Bitmap>(120, 120) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val markerOptions = MarkerOptions()
                            .position(location)
                            .title(user.displayName)
                            .snippet(user.specialization)
                            .icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(resource, isCurrentUser)
                            ))
                            
                        // Make current user's marker bounce
                        if (isCurrentUser) {
                            markerOptions.title("אני (${user.displayName})")
                            markerOptions.zIndex(10f) // Bring to front
                        }
                        
                        val marker = map.addMarker(markerOptions)
                        marker?.tag = user
                        
                        // If current user, make marker bounce
                        if (isCurrentUser && marker != null) {
                            marker.showInfoWindow()
                        }
                        
                        if (marker != null) {
                            markers.add(marker)
                        }
                    }
                    
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Do nothing
                    }
                })
        } else {
            // Use default marker if no profile image
            val markerOptions = MarkerOptions()
                .position(location)
                .title(user.displayName)
                .snippet(user.specialization)
            
            // Make current user's marker special
            if (isCurrentUser) {
                markerOptions.title("אני (${user.displayName})")
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                markerOptions.zIndex(10f) // Bring to front
            }
            
            val marker = map.addMarker(markerOptions)
            marker?.tag = user
            
            // If current user, make marker bounce and show info window
            if (isCurrentUser && marker != null) {
                marker.showInfoWindow()
            }
            
            if (marker != null) {
                markers.add(marker)
            }
        }
    }
    
    /**
     * Creates a circular bitmap marker with the user's profile image
     * 
     * @param profileImage The user's profile image bitmap
     * @param isCurrentUser Whether this marker is for the current user
     * @return A circular bitmap with border for map marker
     */
    private fun createCustomMarker(profileImage: Bitmap, isCurrentUser: Boolean = false): Bitmap {
        // Create a simple circular bitmap
        val size = if (isCurrentUser) 150 else 120 // Larger for current user
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        
        // Create border circle background
        val borderPaint = android.graphics.Paint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.FILL
            color = if (isCurrentUser) android.graphics.Color.BLUE else android.graphics.Color.WHITE
        }
        
        // Draw border circle background
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, borderPaint)
        
        // Scale the profile image to fit
        val scaledBitmap = Bitmap.createScaledBitmap(profileImage, size, size, true)
        
        // Create a shader from the scaled bitmap
        val shaderObj = android.graphics.BitmapShader(scaledBitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP)
        
        // Create a paint with the shader
        val bitmapPaint = android.graphics.Paint().apply {
            isAntiAlias = true
            shader = shaderObj
        }
        
        // Border width
        val borderWidth = if (isCurrentUser) 8f else 4f
        
        // Draw the circular bitmap (slightly smaller than the background for border effect)
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - borderWidth, bitmapPaint)
        
        return output
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        
        // Set up custom info window adapter
        infoWindowAdapter = ProfileInfoWindowAdapter(requireContext()) { userId ->
            // Handle view posts button click
            Toast.makeText(requireContext(), "צפייה בפוסטים של $userId", Toast.LENGTH_SHORT).show()
        }
        map.setInfoWindowAdapter(infoWindowAdapter)
        
        // Enable zoom controls
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        map.uiSettings.isCompassEnabled = true
        
        if (checkLocationPermission()) {
            map.isMyLocationEnabled = true
            moveToCurrentLocation()
        }
        
        setupMapListeners()
        loadMockUsers()
    }

    private fun moveToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
                }
            }
        }
    }

    // Using the newer ActivityResultLauncher approach instead of onRequestPermissionsResult
    private val requestPermissionLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                if (::map.isInitialized) {
                    try {
                        map.isMyLocationEnabled = true
                        moveToCurrentLocation()
                    } catch (e: SecurityException) {
                        // This shouldn't happen since we just checked permission
                        Toast.makeText(requireContext(), "שגיאת הרשאות", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "הרשאות מיקום נדרשות לשיתוף מיקום", Toast.LENGTH_LONG).show()
            }
        }
        
    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onInfoWindowClick(marker: Marker) {
        val user = marker.tag as? User
        if (user != null) {
            // Navigate to PostsActivity to show user's posts
            val intent = Intent(requireContext(), PostsActivity::class.java)
            intent.putExtra("USER_ID", user.uid)
            startActivity(intent)
        }
    }
}