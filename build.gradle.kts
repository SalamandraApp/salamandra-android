// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    id("com.android.application") version "8.3.1" apply false
    alias(libs.plugins.androidApplication) apply false
//    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    alias(libs.plugins.kotlinVersion) apply false
    //KSP
//    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    alias(libs.plugins.googleDevtools) apply false

}

buildscript{
    dependencies{
        classpath(libs.hilt.android.gradle.plugin)
    }
}