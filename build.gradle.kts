plugins {
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "at.o2xfs"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        url = uri("https://repo.fagschlunger.co.at/list/libs-snapshot-local")
    }
    mavenCentral()
}

dependencies {
    api("at.o2xfs:o2xfs-common:1.0-SNAPSHOT")
    implementation("org.apache.commons:commons-lang3:3.9")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}