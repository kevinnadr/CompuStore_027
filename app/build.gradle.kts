plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // 1. TAMBAHKAN PLUGIN VIA ALIAS
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.compustore2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.compustore2"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // 1. Navigasi Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 2. Retrofit (Koneksi ke Backend)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Mengubah JSON jadi Data Object

    // 3. Async Image Loading (Menampilkan gambar dari URL/File)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // 4. ViewModel (Menyimpan data agar tidak hilang saat rotasi layar)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.compose.material:material-icons-extended")
    // 2. WAJIB ADA (LIBRARY UTAMA)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // 3. WAJIB ADA (AGAR BISA DIPAKAI DI RETROFIT)
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}