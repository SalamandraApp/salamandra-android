plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //ksp
    id("com.google.devtools.ksp")
    //Hilt
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.android.salamandra"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.salamandra"
        minSdk = 24
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
            buildConfigField("String", "BASE_URL", "\"https://api.api-ninjas.com/v1/exercises/\"") //This would be the url used in production

        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
            android.buildFeatures.buildConfig = true
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/realms/test/" +
                    "\"") //This would be the url used in debug
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
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //Unit test
    testImplementation("junit:junit:4.13.2")

    //Ui test
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Hilt
    val hiltVersion = "2.49"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-compiler:$hiltVersion")
    //Hilt navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //Destinations
    val destinationsVersion = "1.9.54"
    implementation("io.github.raamcosta.compose-destinations:core:$destinationsVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$destinationsVersion")

    //Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.6.7")

    //Animation
    implementation("androidx.compose.animation:animation:1.6.7")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.3.1")

    //Cognito
    // Amplify core dependency
    implementation ("com.amplifyframework:core-kotlin:2.14.11")
    // Support for Java 8 features
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("com.amplifyframework:aws-auth-cognito:2.15.1")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")



}