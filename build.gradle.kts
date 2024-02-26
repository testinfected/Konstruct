import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
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
        create<MavenPublication>("konstruct") {
            from(components["java"])

            infix fun <T> Property<T>.by(value: T) {
                set(value)
            }

            pom {
                name by project.name
                description by "A test utility to make test data in Kotlin"
                url by "https://github.com/testinfected/konstruct"

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
                    url by "https://github.com/testinfected/konstruct"
                    connection by "https://github.com/testinfected/konstruct.git"
                    developerConnection by "git@github.com:testinfected/konstruct.git"
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype ()
    }
}

signing {
    sign(publishing.publications["konstruct"])
}