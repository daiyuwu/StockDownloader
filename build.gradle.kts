plugins {
    kotlin("jvm") version "1.8.10"
    application
    kotlin("plugin.serialization") version "1.9.0" // ✅ 這行很重要！
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.ty"
//version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // 指定 JDK 版本，例如 17
    }
}

application {
    mainClass.set("com.ty.MainKt")
}

repositories {
//    flatDir {
//        name = "libs.dir"
//        dirs("lib")
//    }
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.9") // SLF4J 簡單日誌實作
    implementation("io.ktor:ktor-client-cio:2.3.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    // https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
    implementation("com.mysql:mysql-connector-j:9.2.0")
}