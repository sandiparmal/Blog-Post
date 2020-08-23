package com.jet2travel.blogpost.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jet2travel.blogpost.R
import com.jet2travel.blogpost.data.Blog
import com.jet2travel.blogpost.data.Media
import com.jet2travel.blogpost.data.User
import com.jet2travel.blogpost.database.DatabaseCache
import com.jet2travel.blogpost.network.ApiClient
import com.jet2travel.blogpost.utils.ConnectivityUtils
import com.jet2travel.blogpost.utils.Constants
import com.jet2travel.blogpost.utils.Constants.Companion.PAGE_LIMIT
import com.jet2travel.blogpost.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

open class BlogRepository {

    companion object {
        private var blogRepository: BlogRepository? = null

        private var gson = Gson()

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
    fun getBlogList(context: Context,
                    databaseCache: DatabaseCache, pageNo : Int
    ): LiveData<Resource<MutableList<Blog>>> {
        val data = MutableLiveData<Resource<MutableList<Blog>>>()

        if (databaseCache.getAllBlogs().isEmpty()) {
            // no data in database, load from server
            data.value = fetchListOfBlogsFromServer(pageNo, data, databaseCache, context)
        } else {
            if (ConnectivityUtils.isNetworkAvailable(context)) {
                // data is present in db but network is present so will load updated data
                Log.d(Constants.APP_TAG, "get data response from Server: $data")
                data.value = fetchListOfBlogsFromServer(pageNo, data, databaseCache, context)
            } else {
                // data is present in db but network is not present so will data from db
                Log.d(Constants.APP_TAG, "get data response from database: $data")

                var vbody = databaseCache.getAllBlogs()
                val rows = mutableListOf<Blog>()
                vbody.forEach { blog ->
                    try{
                        val user: Array<User> = gson.fromJson(blog.user, Array<User>::class.java)
                        val media: Array<Media> = gson.fromJson(blog.media, Array<Media>::class.java)
                        rows.add(
                            Blog(
                                blog.comments,
                                blog.content,
                                blog.createdAt,
                                blog.id.toString(),
                                blog.likes,
                                media.asList(),
                                user.asList()
                            )
                        )
                    } catch (e: Exception){
                        rows.add(
                            Blog(
                                blog.comments,
                                blog.content,
                                blog.createdAt,
                                blog.id.toString(),
                                blog.likes,
                                null,
                                null
                            )
                        )
                    }

                }
                data.value = Resource.success(rows, context.resources.getString(R.string.cached))
            }
        }
        return data
    }

    // Load data from server and saved it in database, delete if any available in db
    private fun fetchListOfBlogsFromServer(
        page: Int,
        data: MutableLiveData<Resource<MutableList<Blog>>>,
        databaseCache: DatabaseCache,
        context: Context
    ): Resource<MutableList<Blog>>? {
        ApiClient.instance.getBlogLists(page, PAGE_LIMIT).enqueue(object :
            Callback<MutableList<Blog>> {
            override fun onResponse(
                call: Call<MutableList<Blog>>,
                response: Response<MutableList<Blog>>
            ) {
                Log.d(Constants.APP_TAG, "data response from server: " + response.body().toString())
                try {
                    if (response.body()?.size!! > 0) {
                        val body: MutableList<Blog> = response.body()!!
                        data.value = Resource.success(body, null)

                        // delete all facts from database and insert updated one
                        if(page == 1){
                            databaseCache.deleteAllBlogs()
                        }

                        val rows = mutableListOf<com.jet2travel.blogpost.database.model.Blog>()
                        val vbody = body.toMutableList()
                        vbody.forEach { blog ->
                            rows.add(
                                com.jet2travel.blogpost.database.model.Blog(
                                    blog.id.toInt(),
                                    blog.comments,
                                    blog.content,
                                    blog.createdAt,
                                    blog.likes,
                                    blog.media.toString(),
                                    blog.user.toString()
                                )
                            )
                        }
                        databaseCache.insert(rows)

                    } else {
                        data.value = Resource.error(
                            context.resources.getString(R.string.no_more_results),
                            null
                        )
                    }
                } catch (e: Exception) {
                    data.value = Resource.error(context.resources.getString(R.string.error), null)
                }
            }

            override fun onFailure(call: Call<MutableList<Blog>>, t: Throwable) {
                Log.d(Constants.APP_TAG, "Error While response : " + t.printStackTrace())
                data.value = Resource.error(
                    context.resources.getString(R.string.network_error),
                    null
                )
            }
        })

        return data.value
    }
}
