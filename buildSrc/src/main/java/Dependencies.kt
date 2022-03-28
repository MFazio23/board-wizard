object Dependencies {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreVersion}"
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"
    const val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hiltVersion}"

    object Compose {
        const val activity = "androidx.activity:activity-compose:${Versions.composeActivityVersion}"
        const val hiltNavigation =
            "androidx.hilt:hilt-navigation-compose:${Versions.composeHiltNavVersion}"
        const val material = "androidx.compose.material:material:${Versions.composeVersion}"
        const val navigation =
            "androidx.navigation:navigation-compose:${Versions.composeNavVersion}"
        const val ui = "androidx.compose.ui:ui:${Versions.composeVersion}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.composeVersion}"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.composeVersion}"
    }

    object Testing {
        const val jUnit = "junit:junit:${Versions.jUnitVersion}"
        const val androidJUnit = "androidx.test.ext:junit:${Versions.androidJUnitVersion}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
        const val composeUITest = "androidx.compose.ui:ui-test-junit4:${Versions.composeVersion}"
    }

}