package com.jet2travel.blogpost.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jet2travel.blogpost.R
import com.jet2travel.blogpost.data.Blog
import com.jet2travel.blogpost.databinding.ItemsBlogBinding

class BlogAdapter(private val blogList: MutableList<Blog>) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    //this method is returning the view or data binding for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val binding = DataBindingUtil.inflate<ItemsBlogBinding>(
            LayoutInflater.from(parent.context), R.layout.items_blog,
            parent, false
        )
        return BlogViewHolder(binding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.binding.blog = blogList[position]
        if (blogList[position].media.size > 0)
            holder.binding.media = blogList[position].media[0]
        holder.binding.user = blogList[position].user[0]
        holder.binding.executePendingBindings()
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return blogList.size
    }

    //the class is holding the list view
    class BlogViewHolder(val binding: ItemsBlogBinding) : RecyclerView.ViewHolder(binding.root)

}