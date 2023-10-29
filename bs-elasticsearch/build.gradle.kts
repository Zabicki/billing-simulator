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
    implementation("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("org.springframework.boot:spring-boot-starter-web:3.0.5")
    implementation("org.springframework.data:spring-data-elasticsearch:5.1.0")
    implementation("co.elastic.clients:elasticsearch-java:8.6.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")

    implementation(project(":core"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}