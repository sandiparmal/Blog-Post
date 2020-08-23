package com.jet2travel.blogpost.ui.fragment

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
import com.jet2travel.blogpost.ui.adapters.EndlessRecyclerOnScrollListener
import com.jet2travel.blogpost.viewmodels.BlogListViewModel


class BlogFragment : Fragment() {

    private lateinit var mBinding: FragmentBlogListBinding
    private var blogAdapter: BlogAdapter? = null
    private var allBlogsList = mutableListOf<Blog>()
    private lateinit var viewModel: BlogListViewModel
    private var pageNo: Int = 1

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

        // Recycler Adapter
        blogAdapter = BlogAdapter(allBlogsList)
        mBinding.recyclerBlogList.adapter = blogAdapter

        //Bind the recyclerview and Add a LayoutManager
        mBinding.recyclerBlogList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        mBinding.recyclerBlogList.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                pageNo += 1
                observeDataChanges(viewModel)
            }
        })

        return mBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BlogListViewModel::class.java)
        observeDataChanges(viewModel)
    }

    // get data from web service and update UI
    private fun observeDataChanges(viewModel: BlogListViewModel) {
        // Update the list when the data changes
        viewModel.getBlogListObservable(activity!!, pageNo, 10)
            .observe(viewLifecycleOwner, Observer<MutableList<Blog>> { blogs ->
                if (blogs != null) {
                    updateUIData(blogs)
                }
            })
    }

    private fun updateUIData(blogList: MutableList<Blog>) {
        try {
            if (blogList.size > 0) {
                mBinding.recyclerBlogList.setItemViewCacheSize(blogList.size)

                if (pageNo == 1) {
                    allBlogsList.clear()
                }

                allBlogsList.addAll(blogList)

                blogAdapter?.notifyDataSetChanged()

            } else {
                Snackbar.make(
                    activity?.window?.decorView?.rootView!!,
                    R.string.no_more_results,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(
                activity?.window?.decorView?.rootView!!,
                R.string.error,
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

}
