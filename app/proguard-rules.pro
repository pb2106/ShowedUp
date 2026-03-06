# ProGuard rules for ShowedUp
# Per PRD FR-12.5.1: R8 full-mode obfuscation — no blanket keeps for crypto classes

# Keep Hilt generated code
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager { *; }

# Keep Room entities
-keep class com.showedup.app.data.local.entity.** { *; }

# Keep Compose
-dontwarn androidx.compose.**

# Keep Kotlin metadata for reflection
-keepattributes RuntimeVisibleAnnotations

# Do NOT blanket-keep crypto utilities — let R8 obfuscate them
