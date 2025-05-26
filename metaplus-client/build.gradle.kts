plugins {
    java
}

dependencies {
    implementation(project(":metaplus-common"))

    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}")
    implementation("org.springframework:spring-web:${Versions.springweb}")
    implementation("org.apache.httpcomponents.client5:httpclient5:${Versions.apachehttpclient}")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}