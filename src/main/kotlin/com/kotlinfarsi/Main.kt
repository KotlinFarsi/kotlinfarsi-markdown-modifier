package com.kotlinfarsi

import java.io.File
import kotlin.system.measureTimeMillis

class Main

const val INPUT_FOLDER = "input"
const val OUTPUT_FOLDER = "output"

fun main() {
    val mainInputDir = System.getProperty("user.dir") + File.separator + INPUT_FOLDER
    val finalOutputDir = convertToOutput(mainInputDir)
    println("Main Input: $mainInputDir")
    println("Final Output: $finalOutputDir")

    val measureTime = measureTimeMillis {
        // remove old output
        File(finalOutputDir).takeIf { it.exists() }?.deleteRecursivelyIf { it.name != ".git" }

        // generate new output
        File(mainInputDir)
            .walkTopDown()
            .filter { it.name != ".git" }
            .forEach { inputFile ->
                val outputDir = convertToOutput(inputFile.path)
                val outputFile = File(outputDir)
                if (shouldNormalize(inputFile)) {
                    val normalizedOutput = Markdown(inputFile)
                    outputFile.writeText(normalizedOutput.content)
                    println("Normalized: $outputDir")
                } else {
                    if (inputFile.name != INPUT_FOLDER) // for skipping an fucking exception ://
                        inputFile.copyTo(outputFile, true)
                }
            }
    }
    println("Files were normalized in $measureTime ms")
}

private fun convertToOutput(inputDir: String) =
    inputDir.replace(INPUT_FOLDER, OUTPUT_FOLDER)

private fun shouldNormalize(file: File) =
    file.path.contains("_tutorials") and "md".equals(file.extension, true)

// inspired by [File.deleteRecursively()]
fun File.deleteRecursivelyIf(clause: (file: File) -> Boolean): Boolean =
    walkBottomUp().fold(true, { res, it -> ((if (clause(it)) it.delete() else true) || !it.exists()) && res })



