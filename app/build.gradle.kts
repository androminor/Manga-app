plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp.plugin)
}

android {
    namespace = "com.androminor.mangaapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.androminor.mangaapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

 androidResources {
     noCompress += "tflite" }
}


dependencies {

    // Core Android
    implementation(libs.androidx.core.ktx.v1101)
    implementation(libs.androidx.lifecycle.runtime.ktx.v261)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose.v172)

    // Compose
    implementation(platform(libs.androidx.compose.bom.v20230300))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.material)
    implementation(libs.material.icons.extended)
    // Navigation
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android.v244)
    implementation(libs.androidx.hilt.navigation.compose.v100)

    // Hilt
    implementation(libs.hilt.android.v244)
    implementation(libs.androidx.hilt.navigation.compose.v100)
    ksp(libs.hilt.compiler) // Add this line to use KSP for Hilt compiler
    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Room
    implementation(libs.androidx.room.runtime)
    //noinspection KaptUsageInsteadOfKsp
    implementation(libs.androidx.room.ktx)

    // Retrofit & OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Coil for image loading
    implementation(libs.coil.compose)

    // CameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // MediaPipe
    implementation(libs.tasks.vision)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20250400))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.util)

    //paging
    implementation(libs.androidx.paging.runtime.v311)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.paging.compose)

    //glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.core)


    // MediaPipe
    implementation(libs.google.mediapipe.tasks.vision)

    // Accompanist
    implementation(libs.google.accompanist.permissions)
    //tensor-flow lite
    implementation(libs.tensorflow.lite)

}
