import org.gradle.api.JavaVersion.VERSION_11

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.secrets.gradle)
    alias(libs.plugins.google.services)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)

}
android {
    namespace = "com.felicksdev.onlymap"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.felicksdev.onlymap"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = VERSION_11
        targetCompatibility = VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.12"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//    var navVersion = "2.7.5"
//    var composeVersion = "1.0.0"
//
//    implementation("androidx.core:core-ktx:1.7.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.8.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
//    implementation("androidx.activity:activity-compose:1.8.0")
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3:1.2.1")
//    implementation("androidx.compose.material:material:1.5.4")
//    implementation("com.google.android.gms:play-services-location:21.0.1")
//    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
//    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
//    implementation("androidx.navigation:navigation-compose:2.5.3")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
//    implementation("com.google.maps.android:maps-compose:4.0.0")
//    implementation("androidx.compose.material:material-icons-extended:1.7.0")
//    implementation("com.google.android.gms:play-services-maps:18.2.0")
//    implementation("com.google.maps.android:android-maps-utils:3.7.0")
//    implementation("com.google.maps.android:maps-utils-ktx:5.0.0")
//    implementation("androidx.recyclerview:recyclerview:1.3.2")
//    implementation("androidx.cardview:cardview:1.0.0")
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.6.4")
//    implementation("androidx.fragment:fragment-ktx:1.6.2")
//    implementation("androidx.activity:activity-ktx:1.8.1")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.5")
//    implementation("androidx.compose.runtime:runtime-livedata:1.7.0")
//
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
//    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime)
    implementation(libs.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.compose.icons)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.maps.utils)
    implementation(libs.maps.utils.ktx)
    implementation(libs.retrofit)
    implementation(libs.gson.converter)
    implementation(libs.fragment.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.livedata.ktx)
    implementation(libs.maps.compose)
    implementation(libs.compose.navigation)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("io.morfly.compose:advanced-bottomsheet-material3:0.1.0")
    implementation(libs.google.accompanist)



    testImplementation(libs.junit)
    androidTestImplementation(libs.test.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

//     DI DEPRECATED
//    implementation(libs.dagger.hilt.android)
//    implementation(libs.dagger.hilt.compose)
//    ksp(libs.dagger.hilt.compiler)
//    implementation(libs.dagger.hilt.compiler)
//    kapt(libs.dagger.hilt.compiler)
//DI
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)

    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

}
