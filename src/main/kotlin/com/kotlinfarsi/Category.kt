package com.kotlinfarsi

enum class Category {
    introduction,
    advance,
    android,
    js,
    native;


    fun getGithubRepoName(): String{
       return when(this){
           introduction -> "OpenSourceTutorials-Introduction"
           advance -> "OpenSourceTutorials-Advanced"
           android -> "OpenSourceTutorials-Android"
           js -> "OpenSourceTutorials-Browser"
           native -> "OpenSourceTutorials-Native"
       }
    }
}