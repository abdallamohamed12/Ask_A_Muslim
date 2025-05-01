plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.abdallamusa.ask_a_muslim"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.abdallamusa.ask_a_muslim"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Retrofit for networking
    implementation (libs.retrofit)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
// Retrofit with Gson converter (for JSON parsing automatically)
    implementation (libs.converter.gson)

// (Optional but recommended) OkHttp for logging network requests
    implementation (libs.logging.interceptor)

    implementation (libs.play.services.location)
}