package com.quadra

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType

class JavaConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            // Apply the Java plugin
            pluginManager.apply(JavaPlugin::class)

            // Default group (can be overridden)
            group = "com.quadra"

            // Configure Java toolchain to use Java 17
            configure<JavaPluginExtension> {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(17))
                }
            }

            // Ensure that annotationProcessor is available on the compileOnly classpath
            configurations.named("compileOnly") {
                extendsFrom(configurations.named("annotationProcessor").get())
            }

            // Repositories (use mavenCentral by default)
            repositories {
                mavenCentral()
            }

            // Add dependencies

            addLombokDependencies()
            addLoggingDependencies()
            addTestingDependencies()

            tasks.withType<JavaCompile>().configureEach {
                options.encoding = "UTF-8"
            }

            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
            }
        }
    }
}

private const val LOMBOK_VERSION = "1.18.38"
private const val SLF4J_VERSION = "2.0.17"
private const val JUNIT_BOM_VERSION = "5.12.2"
private const val ASSERTJ_VERSION = "3.27.3"
private const val MOCKITO_VERSION = "5.17.0"

// Lombok dependencies
private fun Project.addLombokDependencies() {
    dependencies {
        add("compileOnly", "org.projectlombok:lombok:${LOMBOK_VERSION}")
        add("annotationProcessor", "org.projectlombok:lombok:${LOMBOK_VERSION}")
        add("testCompileOnly", "org.projectlombok:lombok:${LOMBOK_VERSION}")
        add("testAnnotationProcessor", "org.projectlombok:lombok:${LOMBOK_VERSION}")
    }
}

/*
 * Logging dependencies
 *
 * Note :
 * - Add slf4j-api ONLY.
 * - The logging implementation must be configured by a consumer.
 */
private fun Project.addLoggingDependencies() {
    dependencies {
        /*
        add("implementation", "ch.qos.logback:logback-classic:$LOGBACK_VERSION")
        add("implementation", "org.apache.logging.log4j:log4j-to-slf4j:$LOG4J_TO_SLF4J_VERSION")
        add("implementation", "org.slf4j:jul-to-slf4j:$com.quadra.SLF4J_VERSION")
         */
        add("compileOnly", "org.slf4j:slf4j-api:${SLF4J_VERSION}")
    }
}

// Testing dependencies
private fun Project.addTestingDependencies() {
    dependencies {
        // JUnit BOM
        add("testImplementation", platform("org.junit:junit-bom:${JUNIT_BOM_VERSION}"))
        // JUnit
        add("testImplementation", "org.junit.jupiter:junit-jupiter")
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
        // AssertJ
        add("testImplementation", "org.assertj:assertj-core:${ASSERTJ_VERSION}")
        // Mockito
        add("testImplementation", "org.mockito:mockito-core:$MOCKITO_VERSION")
        add("testImplementation", "org.mockito:mockito-junit-jupiter:$MOCKITO_VERSION")
    }
}