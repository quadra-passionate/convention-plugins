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

class SpringLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            pluginManager.apply(LibraryConventionPlugin::class)
            pluginManager.apply(SpringBootPlugin::class)
            pluginManager.apply(DependencyManagementPlugin::class)

            dependencies {
                // Spring Core & Logging
                add("implementation", "org.springframework.boot:spring-boot-autoconfigure")
                add("annotationProcessor", "org.springframework.boot:spring-boot-configuration-processor")

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