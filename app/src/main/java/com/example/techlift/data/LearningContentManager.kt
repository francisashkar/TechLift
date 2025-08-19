package com.example.techlift.data

import com.example.techlift.model.*

/**
 * מנהל תוכן הלמידה - מספק שיעורים וקוויזים לכל מסלול
 */
object LearningContentManager {
    
    /**
     * מחזיר את כל השיעורים למסלול מסוים
     */
    fun getLessonsForRoadmap(roadmapId: String): List<Lesson> {
        return when (roadmapId) {
            "frontend" -> getFrontendLessons()
            "backend" -> getBackendLessons()
            "mobile" -> getMobileLessons()
            "devops" -> getDevOpsLessons()
            "ai" -> getAILessons()
            else -> emptyList()
        }
    }
    
    /**
     * מחזיר את הקוויז לשיעור מסוים
     */
    fun getQuizForLesson(lessonId: String): Quiz? {
        return when (lessonId) {
            "frontend_1" -> getFrontendQuiz1()
            "frontend_2" -> getFrontendQuiz2()
            "frontend_3" -> getFrontendQuiz3()
            "backend_1" -> getBackendQuiz1()
            "backend_2" -> getBackendQuiz2()
            "mobile_1" -> getMobileQuiz1()
            "mobile_2" -> getMobileQuiz2()
            "devops_1" -> getDevOpsQuiz1()
            "devops_2" -> getDevOpsQuiz2()
            "ai_1" -> getAIQuiz1()
            "ai_2" -> getAIQuiz2()
            else -> null
        }
    }
    
    fun getAllLessons(): List<Lesson> {
        return getFrontendLessons() + getBackendLessons() + getMobileLessons() + getDevOpsLessons() + getAILessons()
    }
    
    /**
     * מחזיר שיעור לפי מזהה
     */
    fun getLessonById(lessonId: String): Lesson? {
        return getAllLessons().find { it.id == lessonId }
    }
    
    // Frontend Development Lessons
    private fun getFrontendLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "frontend_1",
                roadmapId = "frontend",
                title = "יסודות HTML",
                description = "למד את הבסיס של HTML - תגיות, מבנה דף, וסמנטיקה",
                content = """
                    <h2>מה זה HTML?</h2>
                    <p>HTML (HyperText Markup Language) היא שפת הסימון הבסיסית לבניית דפי אינטרנט.</p>
                    
                    <h3>תגיות בסיסיות:</h3>
                    <ul>
                        <li>&lt;html&gt; - תגית השורש</li>
                        <li>&lt;head&gt; - מידע על הדף</li>
                        <li>&lt;body&gt; - תוכן הדף</li>
                        <li>&lt;h1&gt; עד &lt;h6&gt; - כותרות</li>
                        <li>&lt;p&gt; - פסקאות</li>
                        <li>&lt;div&gt; - מיכל כללי</li>
                    </ul>
                    
                    <h3>דוגמה בסיסית:</h3>
                    <pre><code>&lt;!DOCTYPE html&gt;
&lt;html&gt;
&lt;head&gt;
    &lt;title&gt;הדף שלי&lt;/title&gt;
&lt;/head&gt;
&lt;body&gt;
    &lt;h1&gt;ברוכים הבאים&lt;/h1&gt;
    &lt;p&gt;זה הדף הראשון שלי!&lt;/p&gt;
&lt;/body&gt;
&lt;/html&gt;</code></pre>
                """.trimIndent(),
                duration = 45,
                order = 1,
                quizId = "frontend_1",
                videoUrl = "https://www.youtube.com/watch?v=HD13eq_Pmp8"
            ),
            Lesson(
                id = "frontend_2",
                roadmapId = "frontend",
                title = "CSS - עיצוב וסגנון",
                description = "למד CSS לעיצוב דפי אינטרנט - צבעים, פריסה, ואנימציות",
                content = """
                    <h2>מה זה CSS?</h2>
                    <p>CSS (Cascading Style Sheets) היא שפת העיצוב שמגדירה איך ייראו אלמנטי HTML.</p>
                    
                    <h3>סלקטורים בסיסיים:</h3>
                    <ul>
                        <li>סלקטור אלמנט: <code>p { }</code></li>
                        <li>סלקטור ID: <code>#myId { }</code></li>
                        <li>סלקטור class: <code>.myClass { }</code></li>
                    </ul>
                    
                    <h3>דוגמה לעיצוב:</h3>
                    <pre><code>body {
    font-family: Arial, sans-serif;
    background-color: #f0f0f0;
    margin: 0;
    padding: 20px;
}

h1 {
    color: #333;
    text-align: center;
}

.container {
    max-width: 800px;
    margin: 0 auto;
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}</code></pre>
                """.trimIndent(),
                duration = 60,
                order = 2,
                quizId = "frontend_2",
                videoUrl = "https://www.youtube.com/watch?v=wRNinF7YQqQ"
            ),
            Lesson(
                id = "frontend_3",
                roadmapId = "frontend",
                title = "JavaScript - תכנות אינטראקטיבי",
                description = "למד JavaScript להוספת אינטראקטיביות לדפי אינטרנט",
                content = """
                    <h2>מה זה JavaScript?</h2>
                    <p>JavaScript היא שפת תכנות שמאפשרת להוסיף אינטראקטיביות לדפי אינטרנט.</p>
                    
                    <h3>מושגים בסיסיים:</h3>
                    <ul>
                        <li>משתנים: <code>let name = "ישראל";</code></li>
                        <li>פונקציות: <code>function greet() { }</code></li>
                        <li>אירועים: <code>onclick, onload</code></li>
                    </ul>
                    
                    <h3>דוגמה בסיסית:</h3>
                    <pre><code>// הגדרת משתנה
let userName = "משתמש";

// פונקציה פשוטה
function showMessage() {
    alert("שלום " + userName + "!");
}

// הוספת מאזין אירוע
document.getElementById("myButton").onclick = showMessage;</code></pre>
                """.trimIndent(),
                duration = 75,
                order = 3,
                videoUrl = "https://www.youtube.com/watch?v=lfmg-EJ8gm4"
            )
        )
    }
    
    // Backend Development Lessons
    private fun getBackendLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "backend_1",
                roadmapId = "backend",
                title = "יסודות Node.js",
                description = "למד Node.js לפיתוח שרתים עם JavaScript",
                content = """
                    <h2>מה זה Node.js?</h2>
                    <p>Node.js הוא runtime environment שמאפשר להריץ JavaScript בצד שרת.</p>
                    
                    <h3>יתרונות Node.js:</h3>
                    <ul>
                        <li>JavaScript בשני הצדדים</li>
                        <li>ביצועים גבוהים</li>
                        <li>קהילה גדולה</li>
                        <li>npm - מנהל חבילות</li>
                    </ul>
                    
                    <h3>דוגמה לשרת בסיסי:</h3>
                    <pre><code>const http = require('http');

const server = http.createServer((req, res) => {
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.end('&lt;h1&gt;שלום מהשרת!&lt;/h1&gt;');
});

server.listen(3000, () => {
    console.log('השרת רץ על פורט 3000');
});</code></pre>
                """.trimIndent(),
                duration = 60,
                order = 1,
                quizId = "backend_1",
                videoUrl = "https://www.youtube.com/watch?v=TlB_eWDSMt4"
            ),
            Lesson(
                id = "backend_2",
                roadmapId = "backend",
                title = "Express.js - מסגרת עבודה",
                description = "למד Express.js לבניית APIs ויישומי web",
                content = """
                    <h2>מה זה Express.js?</h2>
                    <p>Express.js היא מסגרת עבודה מינימליסטית ל-Node.js לבניית יישומי web.</p>
                    
                    <h3>התקנה והגדרה:</h3>
                    <pre><code>npm init -y
npm install express</code></pre>
                    
                    <h3>דוגמה בסיסית:</h3>
                    <pre><code>const express = require('express');
const app = express();

// Middleware לפרסור JSON
app.use(express.json());

// נתיב GET
app.get('/', (req, res) => {
    res.json({ message: 'ברוכים הבאים ל-API!' });
});

// נתיב POST
app.post('/users', (req, res) => {
    const { name, email } = req.body;
    res.json({ 
        message: 'משתמש נוצר בהצלחה',
        user: { name, email }
    });
});

app.listen(3000, () => {
    console.log('השרת רץ על פורט 3000');
});</code></pre>
                """.trimIndent(),
                duration = 75,
                order = 2,
                quizId = "backend_2",
                videoUrl = "https://www.youtube.com/watch?v=nH9E25nkk3I"
            )
        )
    }

    // Mobile Development Lessons
    private fun getMobileLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "mobile_1",
                roadmapId = "mobile",
                title = "React Native - יסודות",
                description = "למד React Native לבניית אפליקציות מובייל",
                content = """
                    <h2>מה זה React Native?</h2>
                    <p>React Native היא מסגרת עבודה לבניית אפליקציות מובייל עם JavaScript ו-React.</p>
                    
                    <h3>יתרונות React Native:</h3>
                    <ul>
                        <li>קוד אחד לשני פלטפורמות</li>
                        <li>ביצועים קרובים ל-native</li>
                        <li>קהילה גדולה</li>
                        <li>עדכונים מהירים</li>
                    </ul>
                    
                    <h3>דוגמה בסיסית:</h3>
                    <pre><code>import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const App = () => {
  return (
    &lt;View style={styles.container}&gt;
      &lt;Text style={styles.title}&gt;שלום React Native!&lt;/Text&gt;
    &lt;/View&gt;
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  title: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

export default App;</code></pre>
                """.trimIndent(),
                duration = 60,
                order = 1,
                quizId = "mobile_1",
                videoUrl = "https://www.youtube.com/watch?v=0-S5a0eXPoc"
            ),
            Lesson(
                id = "mobile_2",
                roadmapId = "mobile",
                title = "Flutter - יסודות",
                description = "למד Flutter לבניית אפליקציות מובייל",
                content = """
                    <h2>מה זה Flutter?</h2>
                    <p>Flutter היא מסגרת עבודה של Google לבניית אפליקציות מובייל עם שפת Dart.</p>
                    
                    <h3>יתרונות Flutter:</h3>
                    <ul>
                        <li>ביצועים מעולים</li>
                        <li>UI יפה ועקבי</li>
                        <li>Hot Reload מהיר</li>
                        <li>תמיכה רשמית של Google</li>
                    </ul>
                    
                    <h3>דוגמה בסיסית:</h3>
                    <pre><code>import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() =&gt; _MyHomePageState();
}</code></pre>
                """.trimIndent(),
                duration = 60,
                order = 2,
                quizId = "mobile_2",
                videoUrl = "https://www.youtube.com/watch?v=1ukSR1GRtMU"
            )
        )
    }
    
    // AI Lessons
    private fun getAILessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "ai_1",
                roadmapId = "ai",
                title = "יסודות Python ל-ML",
                description = "למד Python וספריות בסיסיות ללמידת מכונה",
                content = """
                    <h2>Python ללמידת מכונה</h2>
                    <p>Python היא השפה הפופולרית ביותר ללמידת מכונה ובינה מלאכותית.</p>
                    
                    <h3>ספריות חשובות:</h3>
                    <ul>
                        <li>NumPy - חישובים מספריים</li>
                        <li>Pandas - ניתוח נתונים</li>
                        <li>Matplotlib - ויזואליזציה</li>
                        <li>Scikit-learn - אלגוריתמי ML</li>
                    </ul>
                    
                    <h3>דוגמה בסיסית:</h3>
                    <pre><code>import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression

# יצירת נתונים לדוגמה
X = np.random.rand(100, 1) * 10
y = 2 * X + 1 + np.random.randn(100, 1) * 0.5

# חלוקה לנתוני אימון ובדיקה
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# אימון מודל
model = LinearRegression()
model.fit(X_train, y_train)

# חיזוי
predictions = model.predict(X_test)

print(f"דיוק המודל: {model.score(X_test, y_test):.2f}")</code></pre>
                """.trimIndent(),
                duration = 90,
                order = 1,
                quizId = "ai_1",
                videoUrl = "https://www.youtube.com/watch?v=7eh4d6sabA0"
            ),
            Lesson(
                id = "ai_2",
                roadmapId = "ai",
                title = "TensorFlow - למידה עמוקה",
                description = "למד TensorFlow לבניית מודלים של למידה עמוקה",
                content = """
                    <h2>מה זה TensorFlow?</h2>
                    <p>TensorFlow היא ספריית למידת מכונה שפותחה על ידי Google לבניית מודלים של למידת מכונה ולמידה עמוקה.</p>
                    
                    <h3>יתרונות TensorFlow:</h3>
                    <ul>
                        <li>ביצועים גבוהים</li>
                        <li>גמישות רבה</li>
                        <li>קהילה גדולה</li>
                        <li>תמיכה ב-GPU</li>
                        <li>ייצוא מודלים</li>
                    </ul>
                    
                    <h3>דוגמה בסיסית:</h3>
                    <pre><code>import tensorflow as tf
from tensorflow import keras

# יצירת מודל פשוט
model = keras.Sequential([
    keras.layers.Dense(128, activation='relu', input_shape=(784,)),
    keras.layers.Dropout(0.2),
    keras.layers.Dense(10, activation='softmax')
])

# קומפילציה של המודל
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

# אימון המודל
model.fit(x_train, y_train, epochs=5)

# הערכה
test_loss, test_acc = model.evaluate(x_test, y_test)
print(f'דיוק המודל: {test_acc:.2f}')</code></pre>
                """.trimIndent(),
                duration = 90,
                order = 2,
                quizId = "ai_2",
                videoUrl = "https://www.youtube.com/watch?v=6_2hzRopPbQ"
            )
        )
    }

    // DevOps Lessons
    private fun getDevOpsLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "devops_1",
                roadmapId = "devops",
                title = "Docker - יסודות",
                description = "למד Docker לקונטיינריזציה של אפליקציות",
                content = """
                    <h2>מה זה Docker?</h2>
                    <p>Docker הוא פלטפורמה לקונטיינריזציה שמאפשרת לארוז אפליקציות עם כל התלויות שלהן.</p>
                    
                    <h3>יתרונות Docker:</h3>
                    <ul>
                        <li>עקביות בין סביבות</li>
                        <li>בידוד אפליקציות</li>
                        <li>קלות בפריסה</li>
                        <li>יעילות במשאבים</li>
                    </ul>
                    
                    <h3>דוגמה בסיסית:</h3>
                    <pre><code># Dockerfile
FROM node:16-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "start"]

# בניית image
docker build -t my-app .

# הרצת container
docker run -p 3000:3000 my-app</code></pre>
                """.trimIndent(),
                duration = 60,
                order = 1,
                quizId = "devops_1",
                videoUrl = "https://www.youtube.com/watch?v=3c-iBn73dDE"
            ),
            Lesson(
                id = "devops_2",
                roadmapId = "devops",
                title = "Kubernetes - יסודות",
                description = "למד Kubernetes לאורכיסטרציה של קונטיינרים",
                content = """
                    <h2>מה זה Kubernetes?</h2>
                    <p>Kubernetes הוא מערכת אורכיסטרציה לקונטיינרים שמאפשרת ניהול אפליקציות בקנה מידה גדול.</p>
                    
                    <h3>יתרונות Kubernetes:</h3>
                    <ul>
                        <li>ניהול אוטומטי של קונטיינרים</li>
                        <li>סקלביליות אוטומטית</li>
                        <li>עמידות גבוהה</li>
                        <li>ניהול תעבורה חכם</li>
                    </ul>
                    
                    <h3>דוגמה בסיסית:</h3>
                    <pre><code>apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-app
        image: my-app:latest
        ports:
        - containerPort: 3000</code></pre>
                """.trimIndent(),
                duration = 75,
                order = 2,
                quizId = "devops_2",
                videoUrl = "https://www.youtube.com/watch?v=PH-2FfFD2PU"
            )
        )
    }
    
    // Quizzes
    private fun getFrontendQuiz1(): Quiz {
        return Quiz(
            id = "frontend_1",
            lessonId = "frontend_1",
            title = "קוויז HTML בסיסי",
            description = "בדוק את הידע שלך ב-HTML",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "איזו תגית משמשת לכותרת ראשית?",
                    options = listOf("&lt;h1&gt;", "&lt;title&gt;", "&lt;header&gt;", "&lt;head&gt;"),
                    correctAnswerIndex = 0,
                    explanation = "תגית &lt;h1&gt; משמשת לכותרת ראשית. תגית &lt;title&gt; מגדירה את כותרת הדף, &lt;header&gt; היא תגית סמנטית, ו-&lt;head&gt; מכילה מידע על הדף."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "איזו תגית משמשת ליצירת רשימה ממוספרת?",
                    options = listOf("&lt;ul&gt;", "&lt;ol&gt;", "&lt;li&gt;", "&lt;list&gt;"),
                    correctAnswerIndex = 1,
                    explanation = "תגית &lt;ol&gt; (Ordered List) יוצרת רשימה ממוספרת. תגית &lt;ul&gt; יוצרת רשימה לא ממוספרת, &lt;li&gt; היא פריט ברשימה, ו-&lt;list&gt; אינה תגית תקינה."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איזו תגית משמשת ליצירת קישור?",
                    options = listOf("&lt;link&gt;", "&lt;a&gt;", "&lt;url&gt;", "&lt;href&gt;"),
                    correctAnswerIndex = 1,
                    explanation = "תגית &lt;a&gt; (Anchor) משמשת ליצירת קישורים. תגית &lt;link&gt; משמשת לקישור קבצי CSS, ו-&lt;url&gt; ו-&lt;href&gt; אינן תגיות תקינות."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "איזו תגית משמשת להוספת תמונה?",
                    options = listOf("&lt;picture&gt;", "&lt;image&gt;", "&lt;img&gt;", "&lt;figure&gt;"),
                    correctAnswerIndex = 2,
                    explanation = "תגית &lt;img&gt; משמשת להוספת תמונות. &lt;picture&gt; היא תגית מתקדמת לתמונות רספונסיביות, &lt;figure&gt; היא תגית סמנטית, ו-&lt;image&gt; אינה תגית תקינה."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "איזו תגית משמשת ליצירת טבלה?",
                    options = listOf("&lt;table&gt;", "&lt;grid&gt;", "&lt;tab&gt;", "&lt;div&gt;"),
                    correctAnswerIndex = 0,
                    explanation = "תגית &lt;table&gt; משמשת ליצירת טבלאות. &lt;div&gt; היא תגית כללית, ו-&lt;grid&gt; ו-&lt;tab&gt; אינן תגיות תקינות."
                )
            ),
            passingScore = 70
        )
    }
    
    private fun getFrontendQuiz2(): Quiz {
        return Quiz(
            id = "frontend_2",
            lessonId = "frontend_2",
            title = "קוויז CSS בסיסי",
            description = "בדוק את הידע שלך ב-CSS",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "איך מגדירים צבע רקע ב-CSS?",
                    options = listOf("color: red;", "background: red;", "bg-color: red;", "background-color: red;"),
                    correctAnswerIndex = 3,
                    explanation = "התכונה הנכונה היא background-color. color מגדירה צבע טקסט, background היא קיצור, ו-bg-color אינה תכונה תקינה."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "איך יוצרים מרווח של 20 פיקסלים מכל הצדדים?",
                    options = listOf("margin: 20px;", "padding: 20px;", "spacing: 20px;", "gap: 20px;"),
                    correctAnswerIndex = 0,
                    explanation = "margin יוצר מרווח מחוץ לאלמנט. padding יוצר מרווח בתוך האלמנט, ו-spacing ו-gap אינן תכונות CSS תקינות."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איזה סלקטור משמש לבחירת אלמנטים לפי ID?",
                    options = listOf("element", "#id", ".class", "[attribute]"),
                    correctAnswerIndex = 1,
                    explanation = "סלקטור # משמש לבחירת אלמנטים לפי ID. סלקטור . משמש לבחירת אלמנטים לפי class."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "איזו תכונה משמשת להגדרת גופן?",
                    options = listOf("text-family", "font-style", "font-family", "typeface"),
                    correctAnswerIndex = 2,
                    explanation = "font-family היא התכונה הנכונה להגדרת גופן. font-style משמשת להגדרת סגנון (כמו italic)."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "איך מגדירים תמונת רקע?",
                    options = listOf("image: url(...);", "background-image: url(...);", "bg-image: url(...);", "src: url(...);"),
                    correctAnswerIndex = 1,
                    explanation = "background-image היא התכונה הנכונה להגדרת תמונת רקע."
                )
            ),
            passingScore = 70
        )
    }

    private fun getFrontendQuiz3(): Quiz {
        return Quiz(
            id = "frontend_3",
            lessonId = "frontend_3",
            title = "קוויז JavaScript בסיסי",
            description = "בדוק את הידע שלך ב-JavaScript",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "איך מגדירים משתנה ב-JavaScript?",
                    options = listOf("var name", "let name", "const name", "כל התשובות נכונות"),
                    correctAnswerIndex = 3,
                    explanation = "ב-JavaScript המודרני אפשר להשתמש ב-var, let, או const להגדרת משתנים."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "איך יוצרים פונקציה ב-JavaScript?",
                    options = listOf("function myFunc() {}", "const myFunc = () => {}", "const myFunc = function() {}", "כל התשובות נכונות"),
                    correctAnswerIndex = 3,
                    explanation = "ב-JavaScript יש מספר דרכים ליצירת פונקציות: function declaration, arrow function, ו-function expression."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איך בודקים אם משתנה הוא undefined?",
                    options = listOf("if (x == undefined)", "if (x === undefined)", "if (typeof x === 'undefined')", "כל התשובות נכונות"),
                    correctAnswerIndex = 2,
                    explanation = "הדרך הבטוחה ביותר היא typeof x === 'undefined' כי undefined יכול להיות מוגדר מחדש."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "מה זה closure ב-JavaScript?",
                    options = listOf("פונקציה שסוגרת את הדפדפן", "פונקציה שיש לה גישה למשתנים מה-scope החיצוני", "פונקציה שתמיד מחזירה true", "פונקציה שמוגדרת בתוך פונקציה אחרת"),
                    correctAnswerIndex = 1,
                    explanation = "Closure היא פונקציה שיש לה גישה למשתנים מה-scope החיצוני, גם אחרי שהפונקציה החיצונית הסתיימה."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "איך מוסיפים מאזין אירוע ב-JavaScript?",
                    options = listOf("element.onclick = function()", "element.addEventListener('click', function)", "element.click(function)", "כל התשובות נכונות"),
                    correctAnswerIndex = 1,
                    explanation = "addEventListener היא הדרך המודרנית והמומלצת להוספת מאזיני אירועים."
                )
            ),
            passingScore = 70
        )
    }
    
    private fun getBackendQuiz1(): Quiz {
        return Quiz(
            id = "backend_1",
            lessonId = "backend_1",
            title = "קוויז Node.js בסיסי",
            description = "בדוק את הידע שלך ב-Node.js",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "איך מייבאים מודול ב-Node.js?",
                    options = listOf("import module", "require('module')", "include module", "load module"),
                    correctAnswerIndex = 1,
                    explanation = "ב-Node.js משתמשים ב-require() לייבוא מודולים. import משמש ב-ES6 modules, ו-include ו-load אינן פונקציות תקינות."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "איך יוצרים שרת HTTP בסיסי ב-Node.js?",
                    options = listOf("http.createServer()", "server.create()", "new HttpServer()", "Server.create()"),
                    correctAnswerIndex = 0,
                    explanation = "http.createServer() היא הפונקציה הנכונה ליצירת שרת HTTP ב-Node.js."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איזה מודול משמש לעבודה עם נתיבי קבצים?",
                    options = listOf("fs", "path", "file", "dir"),
                    correctAnswerIndex = 1,
                    explanation = "מודול path משמש לעבודה עם נתיבי קבצים. מודול fs משמש לעבודה עם מערכת הקבצים."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "איך קוראים קובץ באופן סינכרוני ב-Node.js?",
                    options = listOf("fs.readFile()", "fs.readFileSync()", "fs.read()", "fs.openSync()"),
                    correctAnswerIndex = 1,
                    explanation = "fs.readFileSync() קורא קובץ באופן סינכרוני. fs.readFile() קורא קובץ באופן אסינכרוני."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "איזה אובייקט מכיל את הפרמטרים של הבקשה ב-HTTP server?",
                    options = listOf("req", "res", "params", "request"),
                    correctAnswerIndex = 0,
                    explanation = "אובייקט req (או request) מכיל את הפרמטרים של הבקשה. אובייקט res (או response) משמש לשליחת תשובה."
                )
            ),
            passingScore = 70
        )
    }
    
    private fun getBackendQuiz2(): Quiz {
        return Quiz(
            id = "backend_2",
            lessonId = "backend_2",
            title = "קוויז Express.js",
            description = "בדוק את הידע שלך ב-Express.js",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "איך מגדירים נתיב GET ב-Express.js?",
                    options = listOf("app.get()", "app.route()", "app.create()", "app.set()"),
                    correctAnswerIndex = 0,
                    explanation = "app.get() היא הפונקציה הנכונה להגדרת נתיב GET ב-Express.js."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "איזה middleware משמש לפרסור גוף בקשת JSON?",
                    options = listOf("express.json()", "express.parser()", "body.json()", "json.parser()"),
                    correctAnswerIndex = 0,
                    explanation = "express.json() הוא ה-middleware הנכון לפרסור גוף בקשת JSON."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איך מגדירים פרמטר בנתיב?",
                    options = listOf("/users/:id", "/users/{id}", "/users/<id>", "/users/[id]"),
                    correctAnswerIndex = 0,
                    explanation = "הסימון :id הוא הנכון להגדרת פרמטר בנתיב ב-Express.js."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "איך ניגשים לפרמטר מהנתיב?",
                    options = listOf("req.param", "req.params", "req.parameters", "req.query"),
                    correctAnswerIndex = 1,
                    explanation = "req.params הוא האובייקט המכיל את הפרמטרים מהנתיב. req.query מכיל את פרמטרי ה-query string."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "איך שולחים תשובת JSON?",
                    options = listOf("res.json()", "res.sendJson()", "res.send(json)", "res.writeJson()"),
                    correctAnswerIndex = 0,
                    explanation = "res.json() היא הפונקציה הנכונה לשליחת תשובת JSON."
                )
            ),
            passingScore = 70
        )
    }

    private fun getMobileQuiz1(): Quiz {
        return Quiz(
            id = "mobile_1",
            lessonId = "mobile_1",
            title = "קוויז React Native",
            description = "בדוק את הידע שלך ב-React Native",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "מה זה React Native?",
                    options = listOf("מסגרת עבודה ל-Android בלבד", "מסגרת עבודה ל-iOS בלבד", "מסגרת עבודה לבניית אפליקציות מובייל עם JavaScript", "שפת תכנות חדשה"),
                    correctAnswerIndex = 2,
                    explanation = "React Native היא מסגרת עבודה שמאפשרת לבנות אפליקציות מובייל עם JavaScript ו-React."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "איך יוצרים component ב-React Native?",
                    options = listOf("class MyComponent extends Component", "function MyComponent()", "const MyComponent = () => {}", "כל התשובות נכונות"),
                    correctAnswerIndex = 3,
                    explanation = "ב-React Native אפשר ליצור components עם class או function syntax."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איזה component משמש לטקסט ב-React Native?",
                    options = listOf("&lt;Text&gt;", "&lt;Label&gt;", "&lt;String&gt;", "&lt;Paragraph&gt;"),
                    correctAnswerIndex = 0,
                    explanation = "&lt;Text&gt; הוא ה-component הנכון לטקסט ב-React Native."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "איך מגדירים סגנונות ב-React Native?",
                    options = listOf("CSS files", "StyleSheet.create()", "inline styles", "כל התשובות נכונות"),
                    correctAnswerIndex = 1,
                    explanation = "StyleSheet.create() היא הדרך המומלצת להגדרת סגנונות ב-React Native."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "מה זה Flexbox ב-React Native?",
                    options = listOf("מערכת פריסה לניהול מיקום אלמנטים", "סוג של גופן", "ספריית אנימציות", "מערכת ניהול מצב"),
                    correctAnswerIndex = 0,
                    explanation = "Flexbox היא מערכת פריסה שמאפשרת לניהול מיקום אלמנטים בצורה גמישה."
                )
            ),
            passingScore = 70
        )
    }

    private fun getMobileQuiz2(): Quiz {
        return Quiz(
            id = "mobile_2",
            lessonId = "mobile_2",
            title = "קוויז Flutter",
            description = "בדוק את הידע שלך ב-Flutter",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "מה זה Flutter?",
                    options = listOf("מסגרת עבודה של Google", "שפת תכנות חדשה", "מסד נתונים", "מערכת הפעלה"),
                    correctAnswerIndex = 0,
                    explanation = "Flutter היא מסגרת עבודה של Google לבניית אפליקציות מובייל."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "באיזו שפה נכתבת אפליקציית Flutter?",
                    options = listOf("JavaScript", "Python", "Dart", "Java"),
                    correctAnswerIndex = 2,
                    explanation = "Flutter נכתבת בשפת Dart שפותחה על ידי Google."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "מה זה Widget ב-Flutter?",
                    options = listOf("כל אלמנט UI", "מסד נתונים", "פונקציה", "משתנה"),
                    correctAnswerIndex = 0,
                    explanation = "ב-Flutter, כל אלמנט UI הוא Widget."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "איך יוצרים StatelessWidget ב-Flutter?",
                    options = listOf("class MyWidget extends StatelessWidget", "function MyWidget()", "const MyWidget = ()", "כל התשובות נכונות"),
                    correctAnswerIndex = 0,
                    explanation = "StatelessWidget נוצר על ידי הרחבה של המחלקה StatelessWidget."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "מה זה Hot Reload ב-Flutter?",
                    options = listOf("עדכון מהיר של האפליקציה ללא הפעלה מחדש", "שינוי צבעים", "עדכון מערכת", "הפעלה מחדש"),
                    correctAnswerIndex = 0,
                    explanation = "Hot Reload מאפשר עדכון מהיר של האפליקציה ללא הפעלה מחדש."
                )
            ),
            passingScore = 70
        )
    }

    private fun getDevOpsQuiz1(): Quiz {
        return Quiz(
            id = "devops_1",
            lessonId = "devops_1",
            title = "קוויז Docker",
            description = "בדוק את הידע שלך ב-Docker",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "מה זה Docker?",
                    options = listOf("מערכת הפעלה", "פלטפורמה לקונטיינריזציה", "מסד נתונים", "שפת תכנות"),
                    correctAnswerIndex = 1,
                    explanation = "Docker היא פלטפורמה לקונטיינריזציה שמאפשרת לארוז אפליקציות עם כל התלויות שלהן."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "מה זה Dockerfile?",
                    options = listOf("קובץ הגדרות לשרת", "הוראות לבניית Docker image", "קובץ קונפיגורציה", "סקריפט הרצה"),
                    correctAnswerIndex = 1,
                    explanation = "Dockerfile מכיל הוראות לבניית Docker image."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איך בונים Docker image?",
                    options = listOf("docker run", "docker build", "docker create", "docker start"),
                    correctAnswerIndex = 1,
                    explanation = "docker build הוא הפקודה לבניית Docker image."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "מה זה Docker Hub?",
                    options = listOf("מסד נתונים", "מאגר Docker images", "שרת Docker", "כלי ניהול"),
                    correctAnswerIndex = 1,
                    explanation = "Docker Hub הוא מאגר ציבורי של Docker images."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "איך מריצים Docker container?",
                    options = listOf("docker start", "docker run", "docker execute", "docker launch"),
                    correctAnswerIndex = 1,
                    explanation = "docker run היא הפקודה להרצת Docker container."
                )
            ),
            passingScore = 70
        )
    }

    private fun getDevOpsQuiz2(): Quiz {
        return Quiz(
            id = "devops_2",
            lessonId = "devops_2",
            title = "קוויז Kubernetes",
            description = "בדוק את הידע שלך ב-Kubernetes",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "מה זה Kubernetes?",
                    options = listOf("מערכת אורכיסטרציה לקונטיינרים", "מסד נתונים", "שרת web", "כלי פיתוח"),
                    correctAnswerIndex = 0,
                    explanation = "Kubernetes היא מערכת אורכיסטרציה לקונטיינרים שמאפשרת ניהול אפליקציות בקנה מידה גדול."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "מה זה Pod ב-Kubernetes?",
                    options = listOf("יחידת הבסיס הקטנה ביותר", "שרת", "מסד נתונים", "רשת"),
                    correctAnswerIndex = 0,
                    explanation = "Pod היא יחידת הבסיס הקטנה ביותר ב-Kubernetes."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איך מגדירים מספר replicas ב-Deployment?",
                    options = listOf("replicas: 3", "instances: 3", "copies: 3", "count: 3"),
                    correctAnswerIndex = 0,
                    explanation = "replicas: 3 מגדיר 3 עותקים של ה-Pod."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "מה זה Service ב-Kubernetes?",
                    options = listOf("שירות לקוחות", "נקודת גישה קבועה ל-Pods", "מסד נתונים", "כלי ניהול"),
                    correctAnswerIndex = 1,
                    explanation = "Service מספק נקודת גישה קבועה ל-Pods."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "איך בודקים את הסטטוס של Pods?",
                    options = listOf("kubectl get pods", "kubectl status pods", "kubectl check pods", "kubectl list pods"),
                    correctAnswerIndex = 0,
                    explanation = "kubectl get pods מציג את כל ה-Pods ואת הסטטוס שלהם."
                )
            ),
            passingScore = 70
        )
    }
    
    private fun getAIQuiz1(): Quiz {
        return Quiz(
            id = "ai_1",
            lessonId = "ai_1",
            title = "קוויז Python ל-ML",
            description = "בדוק את הידע שלך ב-Python ללמידת מכונה",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "איזו ספרייה משמשת לחישובים מספריים ב-Python?",
                    options = listOf("NumPy", "Pandas", "Matplotlib", "Scikit-learn"),
                    correctAnswerIndex = 0,
                    explanation = "NumPy היא הספרייה הבסיסית לחישובים מספריים ב-Python."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "איזו ספרייה משמשת לניתוח נתונים ב-Python?",
                    options = listOf("NumPy", "Pandas", "Matplotlib", "TensorFlow"),
                    correctAnswerIndex = 1,
                    explanation = "Pandas היא הספרייה המובילה לניתוח נתונים ב-Python."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איזו ספרייה משמשת לויזואליזציה של נתונים?",
                    options = listOf("NumPy", "Pandas", "Matplotlib", "Scikit-learn"),
                    correctAnswerIndex = 2,
                    explanation = "Matplotlib היא הספרייה הבסיסית לויזואליזציה של נתונים ב-Python."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "איזו ספרייה משמשת לאלגוריתמי למידת מכונה?",
                    options = listOf("NumPy", "Pandas", "Matplotlib", "Scikit-learn"),
                    correctAnswerIndex = 3,
                    explanation = "Scikit-learn היא הספרייה המובילה לאלגוריתמי למידת מכונה ב-Python."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "איזו ספרייה משמשת ללמידה עמוקה?",
                    options = listOf("TensorFlow", "Pandas", "Matplotlib", "Scikit-learn"),
                    correctAnswerIndex = 0,
                    explanation = "TensorFlow היא אחת הספריות המובילות ללמידה עמוקה."
                )
            ),
            passingScore = 70
        )
    }

    private fun getAIQuiz2(): Quiz {
        return Quiz(
            id = "ai_2",
            lessonId = "ai_2",
            title = "קוויז TensorFlow",
            description = "בדוק את הידע שלך ב-TensorFlow",
            questions = listOf(
                QuizQuestion(
                    id = "q1",
                    questionText = "מה זה TensorFlow?",
                    options = listOf("ספריית למידת מכונה", "מסד נתונים", "שפת תכנות", "מערכת הפעלה"),
                    correctAnswerIndex = 0,
                    explanation = "TensorFlow היא ספריית למידת מכונה שפותחה על ידי Google."
                ),
                QuizQuestion(
                    id = "q2",
                    questionText = "מה זה Tensor ב-TensorFlow?",
                    options = listOf("מערך רב-ממדי", "פונקציה", "משתנה", "אלגוריתם"),
                    correctAnswerIndex = 0,
                    explanation = "Tensor הוא מערך רב-ממדי שמכיל נתונים ב-TensorFlow."
                ),
                QuizQuestion(
                    id = "q3",
                    questionText = "איך יוצרים מודל ב-TensorFlow?",
                    options = listOf("tf.keras.Sequential()", "tf.Model()", "tf.create()", "כל התשובות נכונות"),
                    correctAnswerIndex = 0,
                    explanation = "tf.keras.Sequential() היא הדרך הנפוצה ליצירת מודל ב-TensorFlow."
                ),
                QuizQuestion(
                    id = "q4",
                    questionText = "מה זה Epoch בלמידת מכונה?",
                    options = listOf("סיבוב אחד על כל נתוני האימון", "שלב אחד באלגוריתם", "סוג של מודל", "פונקציית אובדן"),
                    correctAnswerIndex = 0,
                    explanation = "Epoch הוא סיבוב אחד על כל נתוני האימון."
                ),
                QuizQuestion(
                    id = "q5",
                    questionText = "איך מאמנים מודל ב-TensorFlow?",
                    options = listOf("model.fit()", "model.train()", "model.learn()", "model.run()"),
                    correctAnswerIndex = 0,
                    explanation = "model.fit() היא הפונקציה לאימון מודל ב-TensorFlow."
                )
            ),
            passingScore = 70
        )
    }
} 