package com.example.techlift.data

import com.example.techlift.model.Post
import com.example.techlift.model.User
import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * Provides mock data for development and testing
 */
object MockDataProvider {

    // Mock user profile images (placeholder URLs)
    private val profileImages = listOf(
        "https://randomuser.me/api/portraits/men/32.jpg",
        "https://randomuser.me/api/portraits/women/44.jpg",
        "https://randomuser.me/api/portraits/men/67.jpg",
        "https://randomuser.me/api/portraits/women/21.jpg",
        "https://randomuser.me/api/portraits/men/91.jpg",
        "https://randomuser.me/api/portraits/women/56.jpg",
        "https://randomuser.me/api/portraits/men/45.jpg",
        "https://randomuser.me/api/portraits/women/33.jpg",
        "https://randomuser.me/api/portraits/men/22.jpg",
        "https://randomuser.me/api/portraits/women/76.jpg"
    )

    // Mock users with different roles
    val mockUsers = listOf(
        User(
            uid = "user1",
            email = "daniel@techlift.com",
            displayName = "דניאל כהן",
            photoUrl = profileImages[0],
            specialization = "Full Stack Developer",
            experience = "5+ years"
        ),
        User(
            uid = "user2",
            email = "maya@techlift.com",
            displayName = "מאיה לוי",
            photoUrl = profileImages[1],
            specialization = "UX/UI Designer",
            experience = "3 years"
        ),
        User(
            uid = "user3",
            email = "amit@techlift.com",
            displayName = "עמית גולן",
            photoUrl = profileImages[2],
            specialization = "Backend Developer",
            experience = "4 years"
        ),
        User(
            uid = "user4",
            email = "tamar@techlift.com",
            displayName = "תמר אברהם",
            photoUrl = profileImages[3],
            specialization = "Data Analyst",
            experience = "2 years"
        ),
        User(
            uid = "user5",
            email = "yoav@techlift.com",
            displayName = "יואב שפירא",
            photoUrl = profileImages[4],
            specialization = "Frontend Developer",
            experience = "3 years"
        ),
        User(
            uid = "user6",
            email = "noa@techlift.com",
            displayName = "נועה ברק",
            photoUrl = profileImages[5],
            specialization = "DevOps Engineer",
            experience = "4 years"
        ),
        User(
            uid = "user7",
            email = "avi@techlift.com",
            displayName = "אבי מזרחי",
            photoUrl = profileImages[6],
            specialization = "Mobile Developer",
            experience = "6 years"
        ),
        User(
            uid = "user8",
            email = "michal@techlift.com",
            displayName = "מיכל דוד",
            photoUrl = profileImages[7],
            specialization = "QA Engineer",
            experience = "3 years"
        ),
        User(
            uid = "user9",
            email = "roee@techlift.com",
            displayName = "רועי אלון",
            photoUrl = profileImages[8],
            specialization = "Security Specialist",
            experience = "7 years"
        ),
        User(
            uid = "user10",
            email = "shira@techlift.com",
            displayName = "שירה לביא",
            photoUrl = profileImages[9],
            specialization = "Product Manager",
            experience = "5 years"
        ),
        User(
            uid = "current_user",
            email = "me@techlift.com",
            displayName = "אני (משתמש נוכחי)",
            photoUrl = "https://randomuser.me/api/portraits/lego/1.jpg",
            specialization = "TechLift Student",
            experience = "Learning"
        )
    )

    // Mock posts from different users
    val mockPosts = listOf(
        Post(
            id = "post1",
            userId = "user1",
            authorName = "דניאל כהן",
            authorPhotoUrl = profileImages[0],
            title = "טיפים לראיון עבודה בפיתוח Full Stack",
            content = "היי חברים! רציתי לשתף כמה טיפים שעזרו לי בראיונות עבודה לפיתוח Full Stack:\n\n" +
                    "1. התמקדו בהבנת עקרונות בסיסיים ולא רק בטכנולוגיות ספציפיות\n" +
                    "2. תכינו פרויקט אישי שמציג את היכולות שלכם בצד שרת וצד לקוח\n" +
                    "3. תלמדו על ארכיטקטורות נפוצות כמו Microservices\n" +
                    "4. תהיו מוכנים להסביר על האתגרים בפרויקטים קודמים\n\n" +
                    "בהצלחה לכולם! 💻🚀",
            imageUrl = "https://images.unsplash.com/photo-1555099962-4199c345e5dd?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
            likes = 42,
            comments = 7,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -2) }.time
        ),
        Post(
            id = "post2",
            userId = "user2",
            authorName = "מאיה לוי",
            authorPhotoUrl = profileImages[1],
            title = "עקרונות עיצוב UX שכל מפתח צריך להכיר",
            content = "כמעצבת UX/UI, אני רוצה לשתף כמה עקרונות בסיסיים שיעזרו למפתחים ליצור ממשקים טובים יותר:\n\n" +
                    "• פשטות - תמיד עדיף ממשק פשוט וברור\n" +
                    "• עקביות - שמרו על אותם אלמנטים בכל המערכת\n" +
                    "• משוב למשתמש - תמיד תנו למשתמש לדעת מה קורה\n" +
                    "• נגישות - תכננו עבור כל המשתמשים\n\n" +
                    "אשמח לענות על שאלות בנושא! 🎨✨",
            imageUrl = "https://images.unsplash.com/photo-1561070791-2526d30994b5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
            likes = 38,
            comments = 12,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -4) }.time
        ),
        Post(
            id = "post3",
            userId = "user3",
            authorName = "עמית גולן",
            authorPhotoUrl = profileImages[2],
            title = "אופטימיזציה של שאילתות SQL",
            content = "לאחרונה עבדתי על אופטימיזציה של מסד נתונים גדול, והנה כמה טיפים שלמדתי:\n\n" +
                    "1. השתמשו באינדקסים בחוכמה - לא כל עמודה צריכה אינדקס\n" +
                    "2. הימנעו מ-SELECT * - בחרו רק את העמודות שאתם צריכים\n" +
                    "3. השתמשו ב-EXPLAIN לניתוח שאילתות\n" +
                    "4. שקלו שימוש בטבלאות זמניות לשאילתות מורכבות\n\n" +
                    "מי שמתעניין בנושא מוזמן לפנות אלי! 🗄️⚡",
            imageUrl = "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1236&q=80",
            likes = 27,
            comments = 5,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.time
        ),
        Post(
            id = "post4",
            userId = "user4",
            authorName = "תמר אברהם",
            authorPhotoUrl = profileImages[3],
            title = "כלים לויזואליזציה של נתונים",
            content = "היי קהילה! רציתי לשתף כמה כלים מעולים לויזואליזציה של נתונים שאני משתמשת בהם כאנליסטית:\n\n" +
                    "• Tableau - מעולה לדשבורדים אינטראקטיביים\n" +
                    "• Power BI - אינטגרציה טובה עם מוצרי Microsoft\n" +
                    "• Python + Matplotlib/Seaborn - לאוטומציה של דוחות\n" +
                    "• D3.js - לויזואליזציות מותאמות אישית באינטרנט\n\n" +
                    "איזה כלים אתם אוהבים להשתמש בהם? 📊📈",
            imageUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 31,
            comments = 9,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -5) }.time
        ),
        Post(
            id = "post5",
            userId = "user5",
            authorName = "יואב שפירא",
            authorPhotoUrl = profileImages[4],
            title = "מסגרות עבודה מודרניות לפיתוח Frontend",
            content = "בתור מפתח Frontend, אני רוצה לשתף את ההשוואה שלי בין מסגרות העבודה המובילות:\n\n" +
                    "• React - גמישות רבה, אקוסיסטם עשיר, קהילה גדולה\n" +
                    "• Vue - עקומת למידה נוחה, תיעוד מעולה\n" +
                    "• Angular - פתרון מקיף, מתאים לפרויקטים גדולים\n" +
                    "• Svelte - ביצועים מעולים, פחות קוד בוילרפלייט\n\n" +
                    "איזו מסגרת אתם מעדיפים ולמה? 🖥️⚛️",
            imageUrl = "https://images.unsplash.com/photo-1522542550221-31fd19575a2d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 45,
            comments = 15,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -3) }.time
        )
    )

    // Mock user locations for map display
    val mockUserLocations = mapOf(
        "user1" to LatLng(32.0853, 34.7818),  // Tel Aviv
        "user2" to LatLng(31.7683, 35.2137),  // Jerusalem
        "user3" to LatLng(32.7940, 34.9896),  // Haifa
        "user4" to LatLng(31.2518, 34.7915),  // Beer Sheva
        "user5" to LatLng(32.1877, 34.8697),  // Herzliya
        "user6" to LatLng(32.4997, 34.8880),  // Netanya
        "user7" to LatLng(32.0167, 34.7500),  // Bat Yam
        "user8" to LatLng(31.8000, 34.6500),  // Ashdod
        "user9" to LatLng(31.9500, 34.8000),  // Rishon LeZion
        "user10" to LatLng(32.1700, 34.8300), // Ramat Gan
        "current_user" to LatLng(32.0700, 34.7700) // Near Tel Aviv
    )
}
