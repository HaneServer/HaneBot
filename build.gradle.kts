import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
}

group = "net.server-on.hane"
version = "1.0.0"

repositories {
    mavenCentral()
    jcenter()
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:4.4.0_352")
    implementation("com.jagrosh:jda-utilities-command:3.0.3")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}