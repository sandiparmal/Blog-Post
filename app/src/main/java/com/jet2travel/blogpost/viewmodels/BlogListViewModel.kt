package com.jet2travel.blogpost.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jet2travel.blogpost.data.Blog
import com.jet2travel.blogpost.repository.BlogRepository

class BlogListViewModel(application: Application) : AndroidViewModel(application) {

    // Expose LiveData Facts query so the UI can observe it.
    fun getBlogListObservable(context: Context): LiveData<MutableList<Blog>> {
        //return blogListObservable
        return BlogRepository.getInstance().getBlogList(context)
    }

}