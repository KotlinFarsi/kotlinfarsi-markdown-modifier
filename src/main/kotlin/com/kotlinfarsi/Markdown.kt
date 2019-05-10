package com.kotlinfarsi

import java.io.File

data class Markdown(val file: File) {

    var content: String = File(file.path).inputStream().bufferedReader().use { it.readText() }
    val layout = "tutorial"
    lateinit var title: String
    lateinit var category: String
    lateinit var permalink: String

    val frontmatter: String by lazy {
        """---
layout: $layout
title: $title
category: $category
permalink: $permalink
---


"""
    }


    init {

        //finding category
        for (value in Category.values()) {
            if (file.path.contains(value.name))
                category = value.name
        }

        //getting title
        val _title1 = content.substring(content.indexOf("# ") + 2)
        val _title2 = _title1.substring(0, _title1.indexOf("\r\n"))
        title = "\"$_title2\""

        permalink = "/tutorials/$category/${file.parent.substring(file.parent.lastIndexOf("\\") + 1)}"


        //removing first markdown header
        content = content.replace("# $_title2", "")

        //replacing all div rtl s
        content = content.replace("<div dir=\"rtl\">", "<div dir=\"rtl\" markdown=\"1\">")

        content = frontmatter + content
    }
}