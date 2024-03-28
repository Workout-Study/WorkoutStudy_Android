object Libraries {

    object AndroidX {
        const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
        const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
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

}