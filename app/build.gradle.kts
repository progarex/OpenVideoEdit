plugins {
    id("com.android.application")
}

android {
    namespace = "org.libreframe.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.libreframe.app"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "android.test.InstrumentationTestRunner"
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
