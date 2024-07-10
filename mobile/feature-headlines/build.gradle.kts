plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = "com.rogoz208.feature_headlines"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    //  ViewBinding
    implementation(libs.viewbinding.delegate.noreflection)

    //  Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.kotlinx.coroutines.rx3)
    implementation(libs.cicerone)

    //  Dagger
    implementation(libs.dagger.core)
    kapt(libs.dagger.compiler)
    implementation(libs.dagger.injector)

    //  Moxy
    implementation(libs.moxy.core)
    kapt(libs.moxy.compiler)
    implementation(libs.moxy.androidx)

    //  RxJava
    implementation(libs.rxjava3.rxjava)
    implementation(libs.rxjava3.rxandroid)
    implementation(libs.rxjava3.rxkotlin)

    //  Glide
    implementation(libs.glide)

    //  Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //  Modules
    implementation(project(":mobile:feature-headlines:feature-headlines-api"))
    implementation(project(":mobile:navigation:navigation-api"))
    implementation(project(":core:newsdata:newsdata-api"))
    implementation(project(":mobile:mobile-common"))

}