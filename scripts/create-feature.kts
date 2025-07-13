#!/usr/bin/env kotlin

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*

// Get feature name from command line argument
val featureName = if (args.isNotEmpty()) args[0] else {
    println("Usage: kotlin create-feature.kts <FeatureName>")
    kotlin.system.exitProcess(1)
}

val featureNameLower = featureName.lowercase()

println("🚀 Creating feature: $featureName")

// Check if template exists
val templatePath = Paths.get(".template/feature")
if (!templatePath.exists()) {
    println("❌ Error: Template directory not found at .template/feature")
    kotlin.system.exitProcess(1)
}

// Check if target already exists
val targetPath = Paths.get("feature/$featureNameLower")
if (targetPath.exists()) {
    println("❌ Error: Feature '$featureNameLower' already exists")
    kotlin.system.exitProcess(1)
}

println("📋 Step 1: Copying template...")
Files.walk(templatePath).forEach { source ->
    val destination = targetPath.resolve(templatePath.relativize(source))
    if (Files.isDirectory(source)) {
        Files.createDirectories(destination)
    } else {
        Files.copy(source, destination)
    }
}

println("📋 Step 2: Updating file contents...")
Files.walk(targetPath)
    .filter { it.isRegularFile() }
    .filter { path ->
        val extension = path.extension
        extension in listOf("kt", "kts", "xml", "json")
    }
    .forEach { file ->
        val content = file.readText()
        val updatedContent = content
            .replace("TemplateFeature", featureName)
            .replace("templatefeature", featureNameLower)
        file.writeText(updatedContent)
    }

println("📋 Step 3: Renaming directories and files...")
// Rename directories first (from deepest to shallowest)
Files.walk(targetPath)
    .filter { it.isDirectory() }
    .filter { "templatefeature" in it.fileName.toString() }
    .toList()
    .sortedByDescending { it.nameCount }
    .forEach { dir ->
        val newName = dir.fileName.toString().replace("templatefeature", featureNameLower)
        val newDir = dir.parent.resolve(newName)
        Files.move(dir, newDir)
    }

// Rename files
Files.walk(targetPath)
    .filter { it.isRegularFile() }
    .filter { path ->
        val fileName = path.fileName.toString()
        "TemplateFeature" in fileName || "templatefeature" in fileName
    }
    .forEach { file ->
        val newName = file.fileName.toString()
            .replace("TemplateFeature", featureName)
            .replace("templatefeature", featureNameLower)
        val newFile = file.parent.resolve(newName)
        Files.move(file, newFile)
    }

println("📋 Step 4: Updating settings.gradle.kts...")
val settingsFile = Paths.get("settings.gradle.kts")
val settingsContent = settingsFile.readText()
if (!settingsContent.contains(":feature:$featureNameLower:")) {
    val addition = """

// $featureName Feature
include(":feature:$featureNameLower:api")
include(":feature:$featureNameLower:impl")
include(":feature:$featureNameLower:wiring")
include(":feature:$featureNameLower:demo")
"""
    settingsFile.writeText(settingsContent + addition)
    println("✅ Added modules to settings.gradle.kts")
}

println("📋 Step 5: Cleaning build artifacts...")
try {
    val process = ProcessBuilder("./gradlew", "clean")
        .directory(File("."))
        .redirectOutput(ProcessBuilder.Redirect.DISCARD)
        .redirectError(ProcessBuilder.Redirect.DISCARD)
        .start()
    process.waitFor()
} catch (e: Exception) {
    println("⚠️  Warning: Could not clean build artifacts")
}

println()
println("🎉 Successfully created feature: $featureName")
println("📦 Modules created:")
println("   • :feature:$featureNameLower:api")
println("   • :feature:$featureNameLower:impl")
println("   • :feature:$featureNameLower:wiring")
println("   • :feature:$featureNameLower:demo")
println()
println("🏃‍♂️ Next steps:")
println("   1. Sync project: ./gradlew build")
println("   2. Run demo: ./gradlew :feature:$featureNameLower:demo:installDebug")