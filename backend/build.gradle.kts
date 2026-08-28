plugins {
    java
    checkstyle
    id("org.springframework.boot") version "3.4.5" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
    group = "com.blueride"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "checkstyle")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    checkstyle {
        toolVersion = "10.21.4"
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        maxErrors = 0
        maxWarnings = 0
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    tasks.withType<Checkstyle>().configureEach {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}
