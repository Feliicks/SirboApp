
plugins  {
//    id("com.android.application") version "8.3.2" apply false
//    id("com.android.library") version "8.3.2" apply false
//    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
//    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    alias(libs.plugins.android.application) apply  false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.secrets.gradle) apply false
    alias(libs.plugins.compose.compiler) apply false


    alias(libs.plugins.ksp) apply false

//    id("org.jetbrains.kotlin.kapt") version "2.0.0" apply false
//    kotlin("kapt") apply false
//    alias(libs.plugins.devtools.ksp) apply false
//    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.hilt) apply false


}
