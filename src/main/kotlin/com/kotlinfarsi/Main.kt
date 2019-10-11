package com.kotlinfarsi

import java.io.File
import kotlin.system.measureTimeMillis

class Main

fun main() {
    val mainInputDir = Main::class.java.getResource("/website").file
    val finalOutputDir = convertToOutput(mainInputDir)
    println("Main Input: $mainInputDir")
    println("Final Output: $finalOutputDir")

    val measureTime = measureTimeMillis {
        // remove old output
        File(finalOutputDir).takeIf { it.exists() }
            ?.run { listFiles()?.forEach { it.deleteRecursively() } }

        // generate new output
        File(mainInputDir)
            .walk(FileWalkDirection.TOP_DOWN)
            .forEach { inputFile ->
                println(inputFile.path)
                val outputDir = convertToOutput(inputFile.path)
                val outputFile = File(outputDir)
                if (inputFile.path.contains("_tutorials") and "md".equals(inputFile.extension, true)) {
                    println(inputFile.path)
                } else
                    inputFile.copyTo(outputFile, true)
            }
    }
    println("All files were normalized in $measureTime ms")
}

private fun convertToOutput(inputDir: String) =
    inputDir.replace("build${File.separator}resources${File.separator}main${File.separator}website", "output")



