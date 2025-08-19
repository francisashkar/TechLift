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

    // Mock users with different roles - Google Campus Area
    val mockUsers = listOf(
        User(
            uid = "user1",
            email = "daniel@google.com",
            displayName = "דניאל כהן",
            photoUrl = profileImages[0],
            specialization = "Google Software Engineer",
            experience = "5+ years"
        ),
        User(
            uid = "user2",
            email = "maya@google.com",
            displayName = "מאיה לוי",
            photoUrl = profileImages[1],
            specialization = "Google UX Designer",
            experience = "3 years"
        ),
        User(
            uid = "user3",
            email = "amit@google.com",
            displayName = "עמית גולן",
            photoUrl = profileImages[2],
            specialization = "Google Backend Engineer",
            experience = "4 years"
        ),
        User(
            uid = "user4",
            email = "tamar@google.com",
            displayName = "תמר אברהם",
            photoUrl = profileImages[3],
            specialization = "Google Data Scientist",
            experience = "2 years"
        ),
        User(
            uid = "user5",
            email = "yoav@google.com",
            displayName = "יואב שפירא",
            photoUrl = profileImages[4],
            specialization = "Google Frontend Engineer",
            experience = "3 years"
        ),
        User(
            uid = "user6",
            email = "noa@google.com",
            displayName = "נועה ברק",
            photoUrl = profileImages[5],
            specialization = "Google DevOps Engineer",
            experience = "4 years"
        ),
        User(
            uid = "user7",
            email = "avi@google.com",
            displayName = "אבי מזרחי",
            photoUrl = profileImages[6],
            specialization = "Google Mobile Engineer",
            experience = "6 years"
        ),
        User(
            uid = "user8",
            email = "michal@google.com",
            displayName = "מיכל דוד",
            photoUrl = profileImages[7],
            specialization = "Google QA Engineer",
            experience = "3 years"
        ),
        User(
            uid = "user9",
            email = "roee@google.com",
            displayName = "רועי אלון",
            photoUrl = profileImages[8],
            specialization = "Google Security Engineer",
            experience = "7 years"
        ),
        User(
            uid = "user10",
            email = "shira@google.com",
            displayName = "שירה לביא",
            photoUrl = profileImages[9],
            specialization = "Google Product Manager",
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
        ),
        Post(
            id = "post6",
            userId = "user6",
            authorName = "נועה ברק",
            authorPhotoUrl = profileImages[5],
            title = "DevOps: אוטומציה של תהליכי פיתוח",
            content = "היי! כהנדסאית DevOps, אני רוצה לשתף כמה כלים חיוניים לאוטומציה:\n\n" +
                    "• Docker - קונטיינריזציה של אפליקציות\n" +
                    "• Kubernetes - אורכיסטרציה של קונטיינרים\n" +
                    "• Jenkins - אוטומציה של CI/CD\n" +
                    "• Terraform - Infrastructure as Code\n" +
                    "• Prometheus + Grafana - ניטור וביצועים\n\n" +
                    "איך אתם מתחילים עם DevOps? 🐳⚙️",
            imageUrl = "https://images.unsplash.com/photo-1558494949-ef010cbdcc31?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 33,
            comments = 8,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.time
        ),
        Post(
            id = "post7",
            userId = "user7",
            authorName = "אבי מזרחי",
            authorPhotoUrl = profileImages[6],
            title = "פיתוח אפליקציות מובייל - React Native vs Flutter",
            content = "לאחר שנים של פיתוח מובייל, הנה ההשוואה שלי:\n\n" +
                    "React Native:\n" +
                    "• קל למי שיודע React\n" +
                    "• אקוסיסטם עשיר\n" +
                    "• ביצועים טובים\n\n" +
                    "Flutter:\n" +
                    "• ביצועים מעולים\n" +
                    "• UI יפה ועקבי\n" +
                    "• Hot Reload מהיר\n\n" +
                    "מה דעתכם? 📱💻",
            imageUrl = "https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 29,
            comments = 11,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -6) }.time
        ),
        Post(
            id = "post8",
            userId = "user8",
            authorName = "מיכל דוד",
            authorPhotoUrl = profileImages[7],
            title = "בדיקות אוטומטיות - TDD בגישה מעשית",
            content = "כמהנדסת QA, אני מאמינה ב-Test Driven Development:\n\n" +
                    "1. כתבו את הבדיקה לפני הקוד\n" +
                    "2. וודאו שהבדיקה נכשלת\n" +
                    "3. כתבו את הקוד המינימלי לעבור\n" +
                    "4. שפרו את הקוד\n\n" +
                    "זה עוזר לחשוב על המקרים הקצה! 🧪✅",
            imageUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 26,
            comments = 6,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -8) }.time
        ),
        Post(
            id = "post9",
            userId = "user9",
            authorName = "רועי אלון",
            authorPhotoUrl = profileImages[8],
            title = "אבטחת מידע למפתחים - טיפים בסיסיים",
            content = "כמומחה אבטחה, הנה כמה טיפים חשובים:\n\n" +
                    "• השתמשו ב-HTTPS תמיד\n" +
                    "• אל תשמרו סיסמאות בטקסט רגיל\n" +
                    "• השתמשו ב-JWT עם זמן תפוגה קצר\n" +
                    "• בדקו הרשאות משתמש\n" +
                    "• עדכנו תלויות באופן קבוע\n\n" +
                    "אבטחה זה לא אופציונלי! 🔒🛡️",
            imageUrl = "https://images.unsplash.com/photo-1563986768609-322da13575f3?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 41,
            comments = 13,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -9) }.time
        ),
        Post(
            id = "post10",
            userId = "user10",
            authorName = "שירה לביא",
            authorPhotoUrl = profileImages[9],
            title = "ניהול פרויקטים בפיתוח תוכנה",
            content = "נהלת מוצר, הנה מה שלמדתי על ניהול פרויקטים:\n\n" +
                    "• Agile לא תמיד הפתרון הטוב ביותר\n" +
                    "• תקשורת ברורה עם הצוות חשובה\n" +
                    "• תעדו החלטות טכניות\n" +
                    "• תכננו זמן לבדיקות ותיקונים\n" +
                    "• היו גמישים עם שינויים\n\n" +
                    "מה השיטות שאתם מעדיפים? 📋📊",
            imageUrl = "https://images.unsplash.com/photo-1552664730-d307ca884978?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 35,
            comments = 9,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -10) }.time
        ),
        Post(
            id = "post11",
            userId = "user1",
            authorName = "דניאל כהן",
            authorPhotoUrl = profileImages[0],
            title = "ארכיטקטורת Microservices - לקחים מהשטח",
            content = "לאחר שנתיים של עבודה עם Microservices, הנה מה שלמדתי:\n\n" +
                    "• התחילו עם Monolith פשוט\n" +
                    "• חלקו לפי גבולות עסקיים\n" +
                    "• השתמשו ב-API Gateway\n" +
                    "• תכננו לכשלים (Circuit Breaker)\n" +
                    "• ניטור מרכזי הוא קריטי\n\n" +
                    "מי שמתעניין בנושא מוזמן לשיחה! 🏗️🔧",
            imageUrl = "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 28,
            comments = 6,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -12) }.time
        ),
        Post(
            id = "post12",
            userId = "user3",
            authorName = "עמית גולן",
            authorPhotoUrl = profileImages[2],
            title = "NoSQL vs SQL - מתי להשתמש במה?",
            content = "השוואה מעשית בין מסדי נתונים:\n\n" +
                    "SQL מתאים ל:\n" +
                    "• נתונים מובנים ועקביים\n" +
                    "• עסקאות מורכבות\n" +
                    "• דוחות מורכבים\n\n" +
                    "NoSQL מתאים ל:\n" +
                    "• נתונים לא מובנים\n" +
                    "• קנה מידה אופקי\n" +
                    "• גמישות בסכמה\n\n" +
                    "מה דעתכם? 🗄️📊",
            imageUrl = "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1236&q=80",
            likes = 22,
            comments = 4,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -14) }.time
        ),
        Post(
            id = "post13",
            userId = "user5",
            authorName = "יואב שפירא",
            authorPhotoUrl = profileImages[4],
            title = "TypeScript - האם זה שווה את זה?",
            content = "כמפתח Frontend, הנה המחשבות שלי על TypeScript:\n\n" +
                    "יתרונות:\n" +
                    "• פחות באגים בזמן פיתוח\n" +
                    "• IntelliSense מעולה\n" +
                    "• Refactoring בטוח יותר\n\n" +
                    "חסרונות:\n" +
                    "• עקומת למידה נוספת\n" +
                    "• זמן בנייה ארוך יותר\n\n" +
                    "אני ממליץ לכל פרויקט חדש! 💻🔒",
            imageUrl = "https://images.unsplash.com/photo-1522542550221-31fd19575a2d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 31,
            comments = 8,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -16) }.time
        ),
        Post(
            id = "post14",
            userId = "user7",
            authorName = "אבי מזרחי",
            authorPhotoUrl = profileImages[6],
            title = "פיתוח אפליקציות iOS עם SwiftUI",
            content = "SwiftUI שינה את הדרך שאני מפתח iOS:\n\n" +
                    "• קוד פשוט וברור יותר\n" +
                    "• Preview בזמן אמת\n" +
                    "• פחות באגים בממשק\n" +
                    "• אינטגרציה טובה עם iOS\n\n" +
                    "מי שמתחיל עם iOS - זה הזמן! 🍎📱",
            imageUrl = "https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80",
            likes = 19,
            comments = 5,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -18) }.time
        ),
        Post(
            id = "post15",
            userId = "user2",
            authorName = "מאיה לוי",
            authorPhotoUrl = profileImages[1],
            title = "עיצוב למכשירים ניידים - Mobile First",
            content = "כמעצבת UX, הנה העקרונות שלי לעיצוב מובייל:\n\n" +
                    "• התחילו עם מסך קטן\n" +
                    "• כפתורים בגודל מינימלי 44px\n" +
                    "• ניווט פשוט וברור\n" +
                    "• טעינה מהירה חשובה\n" +
                    "• בדקו על מכשירים אמיתיים\n\n" +
                    "מובייל הוא העתיד! 📱🎨",
            imageUrl = "https://images.unsplash.com/photo-1561070791-2526d30994b5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
            likes = 26,
            comments = 7,
            createdAt = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -20) }.time
        )
    )

    // Mock user locations for map display - Google Campus Area, Mountain View, CA
    val mockUserLocations = mapOf(
        "user1" to LatLng(37.4220, -122.0841),  // Googleplex Main Campus
        "user2" to LatLng(37.4230, -122.0830),  // Google Athletic Recreation Field Park
        "user3" to LatLng(37.4215, -122.0855),  // Google Vis Experience
        "user4" to LatLng(37.4200, -122.0860),  // Charleston Campus
        "user5" to LatLng(37.4225, -122.0820),  // Shoreline Amphitheatre Area
        "user6" to LatLng(37.4210, -122.0870),  // Joaquin Rd Area
        "user7" to LatLng(37.4235, -122.0810),  // US-101 Highway Area
        "user8" to LatLng(37.4205, -122.0880),  // Out Burger Area
        "user9" to LatLng(37.4195, -122.0890),  // Zareen's Restaurant Area
        "user10" to LatLng(37.4228, -122.0835), // Google Campus Center
        "current_user" to LatLng(37.4220, -122.0841) // Googleplex (Current User)
    )
}
