object Dependencies {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreVersion}"
    const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"
    const val dataStore =
        "androidx.datastore:datastore-preferences:${Versions.dataStoreVersion}"

    const val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hiltVersion}"

    const val bggApiClient =
        "com.github.MFazio23:bgg-jvm-api-client:${Versions.bggApiClientVersion}"
    const val fazioUtils = "com.github.MFazio23:fazio-utils-jvm:${Versions.fazioUtilsVersion}"

    object Compose {
        const val activity = "androidx.activity:activity-compose:${Versions.composeActivityVersion}"
        const val coilCompose = "io.coil-kt:coil-compose:${Versions.composeCoilVersion}"
        const val hiltNavigation =
            "androidx.hilt:hilt-navigation-compose:${Versions.composeHiltNavVersion}"
        const val lottieCompose = "com.airbnb.android:lottie-compose:${Versions.lottieVersion}"
        const val material = "androidx.compose.material:material:${Versions.composeVersion}"
        const val navigation =
            "androidx.navigation:navigation-compose:${Versions.composeNavVersion}"
        const val runtime = "androidx.compose.runtime:runtime:${Versions.composeRuntimeVersion}"
        const val runtimeLiveData =
            "androidx.compose.runtime:runtime-livedata:${Versions.composeRuntimeVersion}"
        const val ui = "androidx.compose.ui:ui:${Versions.composeVersion}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.composeVersion}"
        const val uiToolingPreview =
            "androidx.compose.ui:ui-tooling-preview:${Versions.composeVersion}"
        const val viewModel =
            "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.composeLifecycleVersion}"
    }

    object Accompanist {
        const val swipeToRefresh =
            "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanistVersion}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.roomVersion}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    }

    object Testing {
        const val jUnit = "junit:junit:${Versions.jUnitVersion}"
        const val androidJUnit = "androidx.test.ext:junit:${Versions.androidJUnitVersion}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
        const val composeUITest = "androidx.compose.ui:ui-test-junit4:${Versions.composeVersion}"
    }

}