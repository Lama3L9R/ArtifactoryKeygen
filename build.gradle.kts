import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "6.0.0"
    application
}

group = "icu.lama"
version = "1.0-SNAPSHOT"
project.setProperty("mainClassName", "icu.lama.artifactory.keygen.KeygenKt")

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-cli:commons-cli:1.8.0")

    // implementation(files("./libs/artifactory-addons-manager-7.9.2.jar")) // initial
    implementation("org.bouncycastle:bcprov-jdk18on:1.74")
    implementation("commons-codec:commons-codec:1.9")
    implementation("org.codehaus.jackson:jackson-core-asl:1.7.9")
    implementation("org.codehaus.jackson:jackson-mapper-asl:1.7.9")

    // JFrog keep using old versions...
    //implementation("org.yaml:snakeyaml:1.23") // using old versions to be able to run artifactory-addons-manager:7.9.2

    implementation(files("./libs/artifactory-addons-manager-7.90.7.jar")) // Update
    implementation("org.yaml:snakeyaml:2.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("org.slf4j:slf4j-api:2.0.7")

    // TODO change to LicenseManager instead of artifactory-addons-manager
    // Which is a de-obfuscated version of artifactory-addons-manager
}

kotlin {
    jvmToolchain(11)
}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "icu.lama.artifactory.keygen.KeygenKt"
    }
}