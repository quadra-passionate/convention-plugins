plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.quadra"
version = "1.0.1"

gradlePlugin {
    plugins {
        register("javaConventionPlugin") {
            id = "com.quadra.java-conventions"
            implementationClass = "com.quadra.JavaConventionPlugin"
        }

        register("libraryConventionPlugin") {
            id = "com.quadra.library-conventions"
            implementationClass = "com.quadra.LibraryConventionPlugin"
        }

        register("springApplicationConventionPlugin") {
            id = "com.quadra.spring-application-conventions"
            implementationClass = "com.quadra.SpringApplicationConventionPlugin"
        }

        register("springLibraryConventionPlugin") {
            id = "com.quadra.spring-library-conventions"
            implementationClass = "com.quadra.SpringLibraryConventionPlugin"
        }
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("Quadra Convention Plugins")
            description.set("Gradle convention plugins for Quadra project")
            url.set("https://github.com/quadra-passionate/convention-plugins")
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/quadra-passionate/convention-plugins")

            credentials {
                username = project.findProperty("gpr.plugin.user") as String?
                    ?: System.getenv("GPR_PLUGIN_USER")
                            ?: error("GPR_PLUGIN_USER must be set in environment")
                password = project.findProperty("gpr.plugin.token") as String?
                    ?: System.getenv("GPR_PLUGIN_TOKEN")
                            ?: error("GPR_PLUGIN_TOKEN must be set in environment")
            }
        }
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.5.3")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
}