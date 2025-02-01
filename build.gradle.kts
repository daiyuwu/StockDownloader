plugins {
    java
    kotlin("jvm") version "1.8.10"
    application
}

group = "org.ty"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // 指定 JDK 版本，例如 17
    }
}

application {
    mainClass.set("MainKt")
}

repositories {
    flatDir {
        name = "libs.dir"
        dirs("lib")
    }
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.mysql:mysql-connector-j:9.2.0")
    implementation(files("lib/yfinance4j-0.1.7.jar"))
//    implementation("io.github.tekichan:yfinance4j:0.1.7")
}