package com.jet2travel.blogpost.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jet2travel.blogpost.database.model.Blog

@Dao
interface AppDao {

    // insert facts in database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blog: MutableList<Blog>)

    // get all facts from database
    @Query("SELECT * FROM blog")
    fun getAllBlogs(): MutableList<Blog>

    //delete all facts from database
    @Query("DELETE FROM blog")
    fun deleteAllBlogs()
}