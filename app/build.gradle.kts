plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = AppConfig.packageName
    compileSdk = AppConfig.compileSDK

    defaultConfig {
        applicationId = AppConfig.packageName
        minSdk = AppConfig.minimumSDK
        targetSdk = AppConfig.targetSDK
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        dataBinding = true
    }
}

dependencies {

    implementation(Libraries.AndroidX.CORE_KTX)
    implementation(Libraries.AndroidX.APP_COMPAT)
    implementation(Libraries.Material.MATERIAL)
    implementation(Libraries.AndroidX.CONSTRAINT_LAYOUT)
    implementation(Libraries.Indicator.INDICATOR)
    implementation(Libraries.Lottie.LOTTIE)
    implementation(platform(Libraries.Firebase.FIREBASE))
    implementation(Libraries.Firebase.ANALYTICS)
    implementation(Libraries.Firebase.STORAGE)
    implementation(Libraries.Glide.GLIDE)
    implementation(Libraries.Glide.BUMPTECH)

    implementation(Libraries.Navigation.NAVIGATION)
    implementation(Libraries.Navigation.NAVIGATION_UI)

    testImplementation(Libraries.Test.JUNIT)

    androidTestImplementation(Libraries.Test.EXT_JUNIT)
    androidTestImplementation(Libraries.Test.ESPRESSO)

}