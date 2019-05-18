package com.kotlinfarsi

import java.io.File
import java.net.URI
import java.net.URL

data class Markdown(val file: File) {

    var content: String = File(file.path).inputStream().bufferedReader().use { it.readText() }
    val layout = "tutorial"
    lateinit var title: String
    lateinit var category: Category
    lateinit var permalink: String
    lateinit var editLink: String
    val imgLines = arrayListOf<String>()

    val frontmatter: String by lazy {
        """---
layout: $layout
title: $title
category: ${category.name}
permalink: $permalink
editlink: $editLink
---


"""
    }


    init {

        //finding category
        //TODO: change this, cause it will be conflict with androidX tutorial
        for (value in Category.values()) {
            if (file.path.contains(value.name))
                category = value
        }

        //getting title
        val _title1 = content.substring(content.indexOf("# ") + 2)
        val _title2 = _title1.substring(0, _title1.indexOf("\r\n"))
        title = "\"$_title2\""

        permalink = "/tutorials/$category/${file.parent.substring(file.parent.lastIndexOf("\\") + 1)}/"

        val windowsPath = file.path.substring(file.path.indexOf("${category.name}") + category.name.length + 1)
        val relativePath = windowsPath.replace("\\","/" )
        editLink = "https://github.com/KotlinFarsi/${category.getGithubRepoName()}/edit/master/src/${relativePath}"

        //removing first markdown header
        content = content.replace("# $_title2", "")

        //replacing all div rtl s
        content = content.replace("<div dir=\"rtl\">", "<div dir=\"rtl\" markdown=\"1\">")

        //replacing default code highlighter
//        content = content.replace("```kotlin","<pre class=\"kotlin-code\" data-target-platform=\"jvm\" theme=\"idea\">\n")
//        content = content.replace("```","</pre>")
//        content = content.replace("</pre>java","```java")
//        content = content.replace("fun main(args: Array<String>)","fun main()")
//      result was failure, the tests were broken
        content = content.replace("<img src=\".","<p style=\"width: calc(100% + 60px);\">\r\n<img src=\"/assets/img/introduction/${file.parent.substring(file.parent.lastIndexOf("\\") + 1)}")
        content = content.replace("/>","/>\r\n</p>")

        content.lines().filter {
            it.contains("<img")
        }.forEach {
            imgLines.add(it)
        }

        content = frontmatter + content


    }
}