package com.quadra

import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.tasks.bundling.BootJar

/**
 *  Convention plugin for Spring REST applications.
 *
 *  Note :
 *  - Spring Security, Spring Cloud dependencies
 *    should be added in the consumer module, not here. (if needed)
 */
class SpringApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            pluginManager.apply(JavaConventionPlugin::class)
            pluginManager.apply(SpringBootPlugin::class)
            pluginManager.apply(DependencyManagementPlugin::class)

            dependencies {
                // Spring Core & Web
                add("implementation", "org.springframework.boot:spring-boot-starter-web")
                add("implementation", "org.springframework.boot:spring-boot-starter-validation")
                add("annotationProcessor", "org.springframework.boot:spring-boot-configuration-processor")

                // Spring Actuator for health check
                add("implementation", "org.springframework.boot:spring-boot-starter-actuator")

                // Testing
                add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
            }

            tasks.named<BootJar>("bootJar") {
                enabled = false
            }

            tasks.named<Jar>("jar") {
                enabled = true
            }
        }
    }
}