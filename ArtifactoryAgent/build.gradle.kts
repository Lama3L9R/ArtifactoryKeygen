import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "icu.lama"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.javassist:javassist:3.29.2-GA")
    implementation("org.jetbrains:annotations:24.0.0")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Premain-Class"] = "icu.lama.artifactory.agent.AgentMain"
        attributes["Main-Class"] = "icu.lama.artifactory.agent.AgentMain"
        attributes["Can-Redefine-Classes"] = true
        attributes["Can-Retransform-Classes"] = true
    }
}