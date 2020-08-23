package com.jet2travel.blogpost.data

data class Blog(
    val comments: Int,
    val content: String,
    val createdAt: String,
    val id: String,
    val likes: Int,
    val media: List<Media>?,
    val user: List<User>?
)