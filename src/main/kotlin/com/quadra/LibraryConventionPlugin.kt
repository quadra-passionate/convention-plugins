package com.quadra

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            // Apply required plugins
            pluginManager.apply(JavaConventionPlugin::class)
            pluginManager.apply(JavaLibraryPlugin::class)
            pluginManager.apply(MavenPublishPlugin::class)

            // Default group (can be overridden)
            group = "com.quadra.common"

            // Configure Maven publishing
            configure<PublishingExtension> {
                // Repositories
                repositories {
                    maven {
                        name = "GitHubPackages"
                        url = uri("https://maven.pkg.github.com/quadra-passionate/common-module")

                        credentials {
                            username = providers.gradleProperty("gpr.module.user")
                                .orElse(providers.environmentVariable("GPR_MODULE_USER"))
                                .orNull

                            password = providers.gradleProperty("gpr.module.token")
                                .orElse(providers.environmentVariable("GPR_MODULE_TOKEN"))
                                .orNull
                        }
                    }
                }
                // Publications
                publications {
                    register<MavenPublication>("gpr") {
                        from(components["java"])
                        groupId = project.group.toString()
                        artifactId = project.name
                        version = project.version.toString()
                        versionMapping {
                            usage("java-api") {
                                fromResolutionOf("runtimeClasspath")
                            }
                            usage("java-runtime") {
                                fromResolutionResult()
                            }
                        }
                    }
                }
            }
        }
    }
}
