plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("com.datastax.oss:java-driver-core:4.15.0")
    implementation("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("org.springframework.boot:spring-boot-starter-web:3.0.5")
    implementation("org.springframework.data:spring-data-cassandra:4.0.5")

    implementation(project(":core"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}