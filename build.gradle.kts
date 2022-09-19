plugins {
    kotlin("jvm") version "1.7.0"
    id("idea")
    id("com.google.protobuf") version "0.8.16"
}

group = "net.server-on.hane"
version = "1.0.0"


repositories {
    mavenCentral()
    jcenter()
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")
}

dependencies {
    implementation("net.dv8tion:JDA:4.4.0_352")
    implementation("com.jagrosh:jda-utilities-command:3.0.3")
    implementation("com.github.walkyst:lavaplayer-fork:1.3.98.4")

    implementation("io.github.cdimascio:dotenv-kotlin:6.3.1")
    implementation("com.google.firebase:firebase-admin:9.0.0")

    implementation("com.google.apis:google-api-services-youtube:v3-rev20220719-2.0.0")
    implementation("com.google.http-client:google-http-client-jackson2:1.42.2")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}