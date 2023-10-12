package com.example.a4basic

data class Note(
    val id: Int,
    var title: String = "",
    var body: String = "",
    var important: Boolean = false
)