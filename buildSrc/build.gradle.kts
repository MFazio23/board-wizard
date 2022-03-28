import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

allprojects {
    repositories {
        gradlePluginPortal()
    }
}