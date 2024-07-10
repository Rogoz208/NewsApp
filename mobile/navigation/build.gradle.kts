plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = "com.rogoz208.navigation"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(libs.androidx.appcompat)

    // Dagger
    implementation(libs.dagger.core)
    kapt(libs.dagger.compiler)
    implementation(libs.dagger.injector)

    //  Cicerone
    implementation(libs.cicerone)

    //  Moxy
    implementation(libs.moxy.core)
    kapt(libs.moxy.compiler)
    implementation(libs.moxy.androidx)

    //  Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(project(":mobile:navigation:navigation-api"))
    implementation(project(":mobile:feature-headlines"))
    implementation(project(":mobile:feature-saved"))
    implementation(project(":mobile:feature-sources"))
    implementation(project(":mobile:feature-filters"))
    implementation(project(":mobile:feature-article"))
    implementation(project(":mobile:mobile-common"))
}