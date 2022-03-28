plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    kotlin("android") version "1.6.10" apply false
    id("dagger.hilt.android.plugin") version "2.41" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}