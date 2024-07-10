@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinKapt)
}

android {
    namespace = "com.rogoz208.newsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rogoz208.newsapp"
        minSdk = 24
        targetSdk = 34
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

    //  Dagger
    implementation(libs.dagger.core)
    kapt(libs.dagger.compiler)
    implementation(libs.dagger.injector)

    //  Cicerone
    implementation(libs.cicerone)

    //  Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //  Modules
    implementation(project(":core:newsapi"))
    implementation(project(":core:newsapi:newsapi-api"))
    implementation(project(":core:database"))
    implementation(project(":core:database:database-api"))
    implementation(project(":core:newsdata"))
    implementation(project(":core:newsdata:newsdata-api"))
    implementation(project(":mobile:navigation"))
    implementation(project(":mobile:navigation:navigation-api"))
    implementation(project(":mobile:feature-headlines"))
    implementation(project(":mobile:feature-headlines:feature-headlines-api"))
    implementation(project(":mobile:feature-saved"))
    implementation(project(":mobile:feature-saved:feature-saved-api"))
    implementation(project(":mobile:feature-sources"))
    implementation(project(":mobile:feature-sources:feature-sources-api"))
    implementation(project(":mobile:feature-filters"))
    implementation(project(":mobile:feature-filters:feature-filters-api"))
    implementation(project(":mobile:feature-article"))
    implementation(project(":mobile:feature-article:feature-article-api"))
}