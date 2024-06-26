// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinVersion) apply false
    //KSP
    alias(libs.plugins.googleDevtools) apply false
}

buildscript{
    dependencies{
        classpath(libs.hilt.android.gradle.plugin)
    }
}