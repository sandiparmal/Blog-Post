package com.jet2travel.blogpost.network

import com.jet2travel.blogpost.data.Blog
import com.jet2travel.blogpost.utils.Constants
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET(Constants.BLOG_URL)
    fun getBlogLists(): Call<MutableList<Blog>>
}