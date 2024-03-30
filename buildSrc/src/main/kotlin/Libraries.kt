object Libraries {

    object AndroidX {
        const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
        const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
        const val ACTIVITY = "androidx.activity:activity-ktx:${Versions.ACTIVITY}"
    }

    object Test {
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
        const val EXT_JUNIT = "androidx.test.ext:junit:${Versions.EXT_JUNIT}"
        const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    }

    object Material {
        const val MATERIAL = "com.google.android.material:material:1.11.0"
    }

    object Indicator {
        const val INDICATOR = "com.tbuonomo:dotsindicator:${Versions.INDICATOR}"
    }

    object Lottie {
        const val LOTTIE = "com.airbnb.android:lottie:${Versions.LOTTIE}"
    }

    object Navigation {
        const val NAVIGATION = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
        const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    }

    object Firebase {
        const val FIREBASE = "com.google.firebase:firebase-bom:${Versions.FIREBASE}"
        const val ANALYTICS = "com.google.firebase:firebase-analytics"
        const val STORAGE = "com.google.firebase:firebase-storage"
    }

    object Glide {
        const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
        const val BUMPTECH = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"
    }

    object Hilt {
        const val HILT = "com.google.dagger:hilt-android:${Versions.HILT}"
        const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT}"
    }

    object Room {
        const val ROOM = "androidx.room:room-ktx:${Versions.ROOM}"
        const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
        const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    }

    object RETROFIT {
        const val RETROFIT_CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    }

}