package com.jet2travel.blogpost.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Data Class
@Entity
data class Blog(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val comments: Int,
    val content: String,
    val createdAt: String,
    val likes: Int,
    val media: String,
    val user: String
)