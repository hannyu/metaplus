plugins {
    java
    id("org.springframework.boot") version "3.4.6"
    id("io.spring.dependency-management") version "1.1.7"
}



configurations {
    compileOnly {
        extendsFrom(annotationProcessor.get())
    }
}

dependencies {
    implementation(project(":metaplus-common"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.apache.httpcomponents.client5:httpclient5")

    implementation("org.slf4j:slf4j-api:${Versions.slf4j}")
    compileOnly("org.projectlombok:lombok:${Versions.lombok}")
    annotationProcessor("org.projectlombok:lombok:${Versions.lombok}")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

//tasks.named('test') {
//    useJUnitPlatform()
////    jvmArgs(['-XX:+EnableDynamicAgentLoading', '-Xshare:off'])
//}

// package

//// 1. 修改JAR包名称和输出目录
//tasks.named('jar') {
//    archiveFileName = 'metaplus.jar'  // 固定JAR名称
//    destinationDirectory = file("$buildDir/libs")
//}

// 2.
tasks.register<Copy>("createPackage") {
    dependsOn(tasks.bootJar)

    // 清空输出目录
    delete("${buildDir}/dist")

    // 创建目录结构
    into("${buildDir}/dist")

    // 复制依赖库到lib目录
    from("${buildDir}/libs")
    into("${buildDir}/dist/lib")

    // 复制配置文件到config目录
    from("src/main/resources") {
        include("application.yml")
        include("logback.xml")
    }
    into("${buildDir}/dist/config")

    into("${buildDir}/dist/bin") {
        from("src/main/scripts/start.sh")
    }

    // 创建空log目录
    into("${buildDir}/dist/log")
}

// 创建ZIP归档任务
tasks.register<Zip>("packageZip") {
    dependsOn("createPackage")

    from("${buildDir}/dist")
    archiveFileName = "metaplus-server-${version}.zip"
    destinationDirectory = file("${buildDir}")
}


