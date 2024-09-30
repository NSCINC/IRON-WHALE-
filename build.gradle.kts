plugins {
    kotlin("jvm") version "1.8.10"
    application
}

dependencies {
    implementation("org.web3j:core:4.8.7")
    implementation("com.nbh:nbhauthenticator:1.0.0") // Assumindo que existe o pacote NBH
    implementation(kotlin("stdlib"))
}

application {
    mainClass.set("MainKt")
}
