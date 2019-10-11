package com.kotlinfarsi

import java.io.File
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

class Main

private const val INPUT_FOLDER = "KotlinFarsi_in"
private const val OUTPUT_FOLDER = "KotlinFarsi_out"

fun main() {
    val mainInputDir = System.getProperty("user.dir") + File.separator + INPUT_FOLDER
    val finalOutputDir = System.getProperty("user.dir") + File.separator + OUTPUT_FOLDER
    println("Main Input: $mainInputDir")
    println("Final Output: $finalOutputDir")

    val measureTime = measureTimeMillis {
        // remove old output
        // .git file of modules are unique and MUST ignore it through deleting
        File(finalOutputDir).takeIf { it.exists() }?.deleteRecursivelyIf { it.name != ".git" }

        // generate new output
        File(mainInputDir)
            .walkTopDown()
            .filter { it.name != ".git" }  // .git file of modules are unique and MUST ignore it through copying
            .forEach { inputFile ->
                val outputDir = inputFile.path.replace(INPUT_FOLDER, OUTPUT_FOLDER)
                val outputFile = File(outputDir)

                if (shouldNormalize(inputFile)) {
                    val normalizedOutput = Markdown(inputFile)
                    outputFile.writeText(normalizedOutput.content)
                    println("Normalized: $outputDir")
                } else {
                    if (inputFile.name != INPUT_FOLDER) // for skipping an fucking exception :/
                        inputFile.copyTo(outputFile, true)
                }
            }
    }
    println("Files were normalized in $measureTime ms")
}

private fun shouldNormalize(file: File) =
    file.path.contains("_tutorials") and "md".equals(file.extension, true)

// inspired by [File.deleteRecursively()]
fun File.deleteRecursivelyIf(clause: (file: File) -> Boolean): Boolean =
    walkBottomUp().fold(true, { res, it -> ((if (clause(it)) it.delete() else true) || !it.exists()) && res })



