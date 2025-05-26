//plugins {
//    id("java")
//}

//group = "com.outofstack.metaplus"
//version = "${Versions.metaplus}"


configurations {
    compileOnly {
        extendsFrom(annotationProcessor.get())
    }
}


dependencies {

    compileOnly("org.projectlombok:lombok:${Versions.lombok}")
    annotationProcessor("org.projectlombok:lombok:${Versions.lombok}")

    compileOnly("org.springframework:spring-web:${Versions.springweb}")
    compileOnly("com.alibaba.fastjson2:fastjson2:${Versions.fastjson2}")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//    testImplementation("com.alibaba.fastjson2:fastjson2:${Versions.fastjson2}")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}")
}
