package com.kotlinfarsi

import java.io.File
import kotlin.system.measureTimeMillis

class Main

fun main() {
    val mainInputDir = System.getProperty("user.dir") + "/input"
    val finalOutputDir = convertToOutput(mainInputDir)
    println("Main Input: $mainInputDir")
    println("Final Output: $finalOutputDir")

    val measureTime = measureTimeMillis {
        // remove old output
        File(finalOutputDir).takeIf { it.exists() }?.deleteRecursively()

        // generate new output
        File(mainInputDir)
            .walk(FileWalkDirection.TOP_DOWN)
            .forEach { inputFile ->
                val outputDir = convertToOutput(inputFile.path)
                val outputFile = File(outputDir)

                if (shouldNormalize(inputFile)) {
                    val normalizedOutput = Markdown(inputFile)
                    outputFile.writeText(normalizedOutput.content)
                    println("Normalized: $outputDir")
                } else
                    inputFile.copyTo(outputFile, true)
            }
    }
    println("Files were normalized in $measureTime ms")
}

private fun convertToOutput(inputDir: String) =
    inputDir.replace("input", "output")

private fun shouldNormalize(file: File) =
    file.path.contains("_tutorials") and "md".equals(file.extension, true)



