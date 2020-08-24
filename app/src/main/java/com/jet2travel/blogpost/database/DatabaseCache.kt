package com.jet2travel.blogpost.database

import android.util.Log
import com.jet2travel.blogpost.database.model.Blog
import com.jet2travel.blogpost.utils.Constants

// Database cache which will call respective dao methods
class DatabaseCache(appDatabase: AppDatabase) {

    private val blogDao = appDatabase.appDao()

    // insert facts in to database
    fun insert(blogs: MutableList<Blog>) {
        Log.d(Constants.APP_TAG, "Inserting: ${blogs.size} blog")
        blogDao.insert(blogs)
    }

    // get all Blogs from database
    fun getAllBlogs(): MutableList<Blog> {
        return blogDao.getAllBlogs()
    }

    // delete all Blogs from database
    fun deleteAllBlogs() {
        blogDao.deleteAllBlogs()
    }
    
}
