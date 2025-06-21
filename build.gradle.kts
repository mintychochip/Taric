plugins {
    java
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
}


group = "org.aincraft"
version = "1.1"

paperweight.reobfArtifactConfiguration =
    io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.glaremasters.me/repository/towny/")
}

dependencies {
    paperweight.paperDevBundle("1.21.5-R0.1-SNAPSHOT")
    implementation("com.h2database:h2:2.2.224")
    compileOnly("com.palmergames.bukkit.towny:towny:0.101.1.0")
    implementation("com.google.inject:guice:7.0.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}

val targetJavaVersion = 21

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    compileJava {
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21) // ✅ Compile for Java 21
}

tasks.named<xyz.jpenilla.runpaper.task.RunServer>("runServer") {
    minecraftVersion("1.21")
}

tasks.named<ProcessResources>("processResources") {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks {
    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")
        destinationDirectory.set(file("C:\\Users\\justi\\Desktop\\paper\\plugins"))
    }
}