plugins {
    java
//    id("org.springframework.boot") version "3.4.6"
//    id("io.spring.dependency-management") version "1.1.7"
}


allprojects {
    // 所有模块（含父模块）的通用配置
    group = "com.outofstack.metaplus"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }

    apply(plugin = "java")  // 默认应用Java插件
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))  // JDK 17
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}

//object Versions {
//    const val metaplus = "0.1.0"
//    const val springweb = "6.2.1"
//    const val fastjson2 = "2.0.54"
//    const val jackson = "2.18.2"
//    const val apachehttpclient = "5.4.1"
//    const val lombok = "1.18.38"
//    const val slf4j = "2.0.17"
//}

//dependencies {
//    // 测试依赖
//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//}

//tasks.test {
//    useJUnitPlatform()
//    jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xshare:off")
//}