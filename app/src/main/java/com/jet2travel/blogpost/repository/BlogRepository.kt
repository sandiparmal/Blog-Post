package com.jet2travel.blogpost.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jet2travel.blogpost.data.Blog
import com.jet2travel.blogpost.network.ApiClient
import com.jet2travel.blogpost.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

open class BlogRepository {

    companion object {
        private var blogRepository: BlogRepository? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): BlogRepository {
            if (blogRepository == null) {
                blogRepository = BlogRepository()
            }
            return blogRepository!!
        }
    }

    // If network is available then load updated data from server and saved it in SQLite database.
    // If network is not available then will load data from SQLite database
    fun getBlogList(context: Context): LiveData<List<Blog>> {
        val data = MutableLiveData<List<Blog>>()

        // data is present in db but network is present so will load updated data
        Log.d(Constants.APP_TAG, "get data response from Server: $data")
        data.value = fetchListOfBlogsFromServer(data, context)

        return data
    }

    // Load data from server and saved it in database, delete if any available in db
    private fun fetchListOfBlogsFromServer(
        data: MutableLiveData<List<Blog>>,
        context: Context
    ): List<Blog>? {
        ApiClient.instance.getBlogLists().enqueue(object : Callback<List<Blog>> {
            override fun onResponse(call: Call<List<Blog>>, response: Response<List<Blog>>) {
                Log.d(Constants.APP_TAG, "data response from server: " + response.body().toString())
                try {
                    if (response.body()?.size!! > 0) {
                        data.value = response.body()
                    }
                } catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<List<Blog>>, t: Throwable) {
                Log.d(Constants.APP_TAG, "Error While response : " + t.printStackTrace())

            }
        })

        return data.value
    }
}
