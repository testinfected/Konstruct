import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test"))
    testImplementation("com.natpryce:hamkrest:1.8.0.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("failed", "standardOut")
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("flintstone") {
            from(components["java"])

            infix fun <T> Property<T>.by(value: T) {
                set(value)
            }

            pom {
                name by project.name
                description by "A tiny library to simplify writing test data builders in Kotlin"
                url by "https://github.com/testinfected/flinstone"

                licenses {
                    license {
                        name by "BSD 3-Clause License"
                        url by "https://opensource.org/licenses/BSD-3-Clause"
                        distribution by "repo"
                    }
                }

                developers {
                    developer {
                        id by "testinfected"
                        name by "Vincent Tencé"
                        organization by "Bee Software"
                        organizationUrl by "http://bee.software"
                    }
                }

                scm {
                    url by "https://github.com/testinfected/flintstone"
                    connection by "https://github.com/testinfected/flintstone.git"
                    developerConnection by "git@github.com:testinfected/flintstone.git"
                }

            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
        }
    }
}

signing {
    sign(publishing.publications["flintstone"])
}