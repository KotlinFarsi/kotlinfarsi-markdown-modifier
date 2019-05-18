package com.kotlinfarsi

import java.io.File

class Main

fun main() {

    val start = System.currentTimeMillis()

    val introductionRootFolderPath = Main::class.java.getResource("/introduction").file
    val markdowns = File(introductionRootFolderPath)
        .walk(FileWalkDirection.TOP_DOWN)
        .filter {
            it.path.contains("Readme.md", true) and
                    !(it.path.toString().contains("introduction\\Readme.md", true))
        }.map {
            Markdown(it)
        }.forEach {
            val outputPath = it.file.path.replace("\\build\\resources\\main" , "\\output")
            File(outputPath).writeText(it.content)
            println(outputPath)
        }

    val end = System.currentTimeMillis()

    println(end - start)

}


