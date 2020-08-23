package com.jet2travel.blogpost.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.jet2travel.blogpost.R
import com.jet2travel.blogpost.data.Blog
import com.jet2travel.blogpost.databinding.FragmentBlogListBinding
import com.jet2travel.blogpost.ui.adapters.BlogAdapter
import com.jet2travel.blogpost.viewmodels.BlogListViewModel


class BlogFragment : Fragment() {

    private lateinit var mBinding: FragmentBlogListBinding
    private var blogAdapter: BlogAdapter? = null
    private var blogList = mutableListOf<Blog>()
    private lateinit var viewModel: BlogListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


// Define the binding
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_blog_list, container, false)

        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BlogListViewModel::class.java)
        observeViewModel(viewModel)
    }

    // get data from web service and update UI
    private fun observeViewModel(viewModel: BlogListViewModel) {
        // Update the list when the data changes
        viewModel.getBlogListObservable(activity!!)
            .observe(viewLifecycleOwner, Observer<MutableList<Blog>> { blogs ->
                if (blogs != null) {
                    updateUIData(blogs)
                }
            })
    }

    private fun updateUIData(blogList: MutableList<Blog>) {

        if (blogList.size > 0) {
            mBinding.recyclerBlogList.setItemViewCacheSize(blogList.size)
            this.blogList = blogList

            // Recycler Adapter
            blogAdapter = BlogAdapter(blogList)
            mBinding.recyclerBlogList.adapter = blogAdapter

            //Bind the recyclerview and Add a LayoutManager
            mBinding.recyclerBlogList.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            // clear list, add new items in list and refresh it using notifyDataSetChanged
        }
    }

}
