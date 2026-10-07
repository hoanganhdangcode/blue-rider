plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidLint) apply false
}

tasks.register("createXCFrameworks") {
    group = "distribution"
    description = "Assemble ridecore and rideui XCFrameworks for distribution"
    dependsOn(
        ":ridecore:assembleRidecoreKitXCFramework",
        ":rideui:assembleRideuiKitXCFramework",
    )
}

tasks.register("createAars") {
    group = "distribution"
    description = "Assemble ridecore and rideui Android AARs for distribution"
    dependsOn(
        ":ridecore:bundleAndroidMainAar",
        ":rideui:bundleAndroidMainAar",
    )
}