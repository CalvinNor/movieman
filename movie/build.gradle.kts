plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinParcelize)
    id(Plugins.kotlinAnnotationProcessor)
    id(Plugins.safeArgs)
}

android {
    compileSdk = App.compileSdk

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        minSdk = App.minSdk
        targetSdk = App.targetSdk
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    implementation(project(Modules.core))
    implementation(project(Modules.data))
    implementation(project(Modules.settings))

    // TODO: Move to a Gradle script to be imported from all features

    // Testing
    testImplementation(Dependencies.test_JUnit)
    testImplementation(Dependencies.test_LiveData)
    testImplementation(Dependencies.test_coRoutines)
    testImplementation(Dependencies.test_MockitoKotlin)

    // Android Testing
    androidTestImplementation(Dependencies.androidTest_runner)
    androidTestImplementation(Dependencies.androidTest_espresso)

}
