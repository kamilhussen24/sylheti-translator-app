# Kotlin
-dontwarn kotlin.**
-keep class kotlin.** { *; }

# OkHttp / Okio
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Lottie
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** { *; }

# AndroidX
-dontwarn androidx.**
-keep class androidx.** { *; }

# App classes
-keep class co.median.android.jlrnql.** { *; }
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable