plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // 🔑 KAPT für Room
    id("kotlin-kapt")

    alias(libs.plugins.google.services)
}


android {
    namespace = "de.geier.citymanager"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "de.geier.citymanager"
        minSdk = 26
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

    // ─────────────────────────────────────────
    // Core Android
    // ─────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ─────────────────────────────────────────
    // Compose BOM (wichtig!)
    // ─────────────────────────────────────────
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // ✅ Material 3 (für SmallTopAppBar, Scaffold etc.)
    implementation(libs.androidx.compose.material3)

    // ─────────────────────────────────────────
    // Debug / Tests
    // ─────────────────────────────────────────
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // ─────────────────────────────────────────
    // Firebase (Variante 2 – Live Content)
    // ─────────────────────────────────────────
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)

    // ─────────────────────────────────────────
    // 🖼️ Coil (Bilder laden per URI)
    // ─────────────────────────────────────────
    implementation("io.coil-kt:coil-compose:2.7.0")
    // ─────────────────────────────────────────
// 🧭 Navigation (Compose)
// ─────────────────────────────────────────
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // ─────────────────────────────────────────
// 🗄️ Room (Persistenz – Vorbereitung)
// ─────────────────────────────────────────
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

}
