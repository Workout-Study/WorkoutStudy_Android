import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
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

        val apiKeys = listOf("NAVER_CLIENT", "NAVER_SECRET", "KAKAO_NATIVE_APP_KEY", "KAKAO_REST_API_KEY", "KAKAO_ADMIN_KEY", "CHAT_SERVER_ADDRESS")

        apiKeys.forEach { key ->
            val value = getApiKey(key)
            buildConfigField("String", key, value)
        }

//        buildConfigField("String", "NAVER_CLIENT", getApiKey("NAVER_CLIENT"))
//        buildConfigField("String", "NAVER_SECRET", getApiKey("NAVER_SECRET"))
//        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getApiKey("KAKAO_NATIVE_APP_KEY"))
//        buildConfigField("String", "KAKAO_REST_API_KEY", getApiKey("KAKAO_REST_API_KEY"))
//        buildConfigField("String", "KAKAO_ADMIN_KEY", getApiKey("KAKAO_ADMIN_KEY"))
//        buildConfigField("String", "CHAT_SERVER_ADDRESS", getApiKey("CHAT_SERVER_ADDRESS"))
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
        buildConfig = true
    }
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {

    implementation(Libraries.AndroidX.CORE_KTX)
    implementation(Libraries.AndroidX.APP_COMPAT)
    implementation(Libraries.Material.MATERIAL)
    implementation(Libraries.AndroidX.CONSTRAINT_LAYOUT)
    implementation(Libraries.AndroidX.ACTIVITY)

    implementation(Libraries.Indicator.INDICATOR)
    implementation(Libraries.Lottie.LOTTIE)
    implementation(platform(Libraries.Firebase.FIREBASE))
    implementation(Libraries.Firebase.ANALYTICS)
    implementation(Libraries.Firebase.STORAGE)
    implementation(Libraries.Glide.GLIDE)
    implementation(Libraries.Glide.BUMPTECH)
    implementation(Libraries.RETROFIT.RETROFIT_CONVERTER_GSON)

    implementation(Libraries.Hilt.HILT)
    kapt(Libraries.Hilt.HILT_COMPILER)

    implementation(Libraries.Room.ROOM)
    implementation(Libraries.Room.ROOM_RUNTIME)
    annotationProcessor(Libraries.Room.ROOM_COMPILER)
    kapt(Libraries.Room.ROOM_COMPILER)

    implementation(Libraries.Navigation.NAVIGATION)
    implementation(Libraries.Navigation.NAVIGATION_UI)

    implementation("com.github.Tans5:horizontalnestedscrollview:0.1.0")
    implementation("id.zelory:compressor:3.0.1")

    testImplementation(Libraries.Test.JUNIT)

    androidTestImplementation(Libraries.Test.EXT_JUNIT)
    androidTestImplementation(Libraries.Test.ESPRESSO)

}