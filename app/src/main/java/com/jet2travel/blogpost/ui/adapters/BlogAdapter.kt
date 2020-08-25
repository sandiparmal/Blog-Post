package com.jet2travel.blogpost.ui.adapters

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jet2travel.blogpost.R
import com.jet2travel.blogpost.data.Blog
import com.jet2travel.blogpost.databinding.ItemsBlogBinding
import com.jet2travel.blogpost.databinding.LoadingItemBinding


class BlogAdapter(private val blogList: MutableList<Blog?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private lateinit var mContext: Context

    //this method is returning the view or data binding for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        if (viewType === VIEW_TYPE_ITEM) {
            val binding = DataBindingUtil.inflate<ItemsBlogBinding>(
                LayoutInflater.from(parent.context), R.layout.items_blog,
                parent, false
            )
            return BlogViewHolder(binding)
        } else {  //        } else if (viewType === VIEW_TYPE_LOADING) {
            val binding = DataBindingUtil.inflate<LoadingItemBinding>(
                LayoutInflater.from(parent.context), R.layout.loading_item,
                parent, false
            )
            return LoadingViewHolder(binding)
        }
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BlogViewHolder) {
            holder.binding.blog = blogList[position]
            Log.d("--> $position", blogList[position].toString())
            if (blogList[position] != null && blogList[position]?.user != null && blogList[position]?.user?.size!! > 0) {
                holder.binding.user = blogList[position]?.user!![0]
            }
            if (blogList[position] != null && blogList[position]?.media != null && blogList[position]?.media?.size!! > 0) {
                holder.binding.media = blogList[position]?.media!![0]
            }
            holder.binding.executePendingBindings()
        } else if (holder is LoadingViewHolder) {
            holder.binding.progressBar.isIndeterminate = true
        }
    }


    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return blogList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (blogList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    //Adding loading item
    fun addLoadingView() {
//        Handler().post {
        blogList.add(null)
        notifyItemInserted(blogList.size - 1)
//        }
    }

    //Remove loading item
    fun removeLoadingView() {
        if (blogList.size > 0) {
            blogList.removeAt(blogList.size - 1)
            notifyItemRemoved(blogList.size)
        }
    }

    //the class is holding the list view
    class BlogViewHolder(val binding: ItemsBlogBinding) : RecyclerView.ViewHolder(binding.root)

    class LoadingViewHolder(val binding: LoadingItemBinding) : RecyclerView.ViewHolder(binding.root)

}