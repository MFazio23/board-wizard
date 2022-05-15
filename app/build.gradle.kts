plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        applicationId = "dev.mfazio.boardwizard"
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeVersion
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.hilt)
    implementation(Dependencies.lifecycleKtx)
    implementation(Dependencies.liveDataKtx)
    implementation(Dependencies.timber)
    implementation(Dependencies.dataStore)

    implementation(Dependencies.Room.runtime)
    implementation(Dependencies.Room.roomKtx)

    implementation(Dependencies.bggApiClient)
    implementation(Dependencies.fazioUtils)

    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.coilCompose)
    implementation(Dependencies.Compose.hiltNavigation)
    implementation(Dependencies.Compose.lottieCompose)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.navigation)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiToolingPreview)
    implementation(Dependencies.Compose.runtimeLiveData)
    implementation(Dependencies.Compose.viewModel)
    implementation(Dependencies.Accompanist.swipeToRefresh)

    kapt(Dependencies.Room.roomCompiler)
    kapt(Dependencies.hiltCompiler)

    testImplementation(Dependencies.Testing.jUnit)
    androidTestImplementation(Dependencies.Testing.androidJUnit)
    androidTestImplementation(Dependencies.Testing.espresso)
    androidTestImplementation(Dependencies.Testing.composeUITest)

    debugImplementation(Dependencies.Compose.uiTooling)
}

kapt {
    correctErrorTypes = true
}