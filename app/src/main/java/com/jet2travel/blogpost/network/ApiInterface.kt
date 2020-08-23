package com.jet2travel.blogpost.network

import com.jet2travel.blogpost.data.Blog
import com.jet2travel.blogpost.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET(Constants.BLOG_URL)
    fun getBlogLists(@Query("page") page: Int,
                     @Query("limit") limit: Int): Call<MutableList<Blog>>


}