plugins {
    java
    id("com.github.ben-manes.versions")
    id("com.diffplug.spotless")
}

allprojects {
    apply(plugin="idea")
    apply(plugin = "com.diffplug.spotless")

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin="java")

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }
    }
}