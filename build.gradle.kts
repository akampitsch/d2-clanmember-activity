import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "discord"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

plugins {
    val kotlinVersion = "1.6.10"
    val springBootVersion = "2.6.3"
    val springDependencyManagement = "1.0.11.RELEASE"
    
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    
    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version springDependencyManagement
    
    application
}

val kordVersion = "0.8.0-M10"
val kotlinxVersion = "1.6.0"
val jacksonVersion = "2.13.1"
val log4j2KotlinVersion = "1.1.0"
        
dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mustache")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$kotlinxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxVersion")

    implementation("org.apache.logging.log4j:log4j-api-kotlin:$log4j2KotlinVersion")

    implementation("com.h2database:h2")

    implementation("org.apache.commons:commons-lang3")

    implementation("dev.kord:kord-core:$kordVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

repositories {
    mavenCentral()
    // Kord Snapshots Repository (Optional):
    maven("https://oss.sonatype.org/content/repositories/snapshots")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn")
        jvmTarget = "11"
    }
}

application {
    mainClass.set("DiscordBoy.kt")
}