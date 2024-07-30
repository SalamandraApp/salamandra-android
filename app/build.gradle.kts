plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinVersion)
    //ksp
    alias(libs.plugins.googleDevtools)
    //Hilt
    id("dagger.hilt.android.plugin")
    //SQLDelight
    id("app.cash.sqldelight") version "2.0.2"
}

sqldelight {
    databases {
        create("SalamandraLocalDB") {
            packageName.set("com.android.salamandra")
        }
    }
}

android {
    namespace = "com.android.salamandra"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.salamandra"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            buildFeatures.buildConfig = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://z1xb9bwrh1.execute-api.eu-west-3.amazonaws.com/dev/\"") //This would be the url used in production

        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
            android.buildFeatures.buildConfig = true
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:3000/" + "\"") //Use this url when using virtual emulator
//             buildConfigField("String", "BASE_URL", "\"http://127.0.0.1:3000/" + "\"") //Use this url when using physical emulator
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.compose.bom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.material3)

    //Unit test
    testImplementation(libs.junit)
    testImplementation(libs.kotlintest.runner.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    //mockk
    testImplementation(libs.mockk)

    //Ui test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.compose.bom)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    //Hilt navigation
    implementation(libs.androidx.hilt.navigation.compose)

    //Destinations
    implementation(libs.core)
    ksp(libs.ksp)

    //Icons Extended
    implementation(libs.androidx.material.icons.extended)

    //Animation
    implementation(libs.androidx.animation)

    //DataStore
    implementation(libs.androidx.datastore.preferences)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Interceptor
    implementation(libs.logging.interceptor)

    //Cognito
    // Amplify core dependency
    implementation (libs.core.kotlin)
    // Support for Java 8 features
    coreLibraryDesugaring (libs.desugar.jdk.libs)
    implementation(libs.aws.auth.cognito)

    //DataStore
    implementation(libs.androidx.datastore.preferences)
    
    //SLQDelight
    implementation("app.cash.sqldelight:android-driver:2.0.2")
    implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
    //Turbine for testing flows
    androidTestImplementation("app.cash.turbine:turbine:1.1.0")

}