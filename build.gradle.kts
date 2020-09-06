plugins {
    kotlin("jvm") version "1.3.72"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("junit", "junit", "4.12")

    testImplementation("org.hamcrest:hamcrest:2.2")

    testImplementation("org.mockito:mockito-core:2.7.22")
}
