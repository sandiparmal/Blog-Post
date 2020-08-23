package com.jet2travel.blogpost.ui.fragment

import android.os.Bundle
import android.os.Handler
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
import com.jet2travel.blogpost.database.Injector
import com.jet2travel.blogpost.databinding.FragmentBlogListBinding
import com.jet2travel.blogpost.ui.adapters.BlogAdapter
import com.jet2travel.blogpost.ui.adapters.EndlessRecyclerOnScrollListener
import com.jet2travel.blogpost.utils.Resource
import com.jet2travel.blogpost.utils.Status
import com.jet2travel.blogpost.viewmodels.BlogListViewModel


class BlogFragment : Fragment() {

    private lateinit var mBinding: FragmentBlogListBinding
    private var blogAdapter: BlogAdapter? = null
    private var blogList = mutableListOf<Blog?>()
    private lateinit var viewModel: BlogListViewModel
    private var pageno: Int = 1
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Define the binding
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_blog_list, container, false)

        // Recycler Adapter
        blogAdapter = BlogAdapter(blogList)
        mBinding.recyclerBlogList.adapter = blogAdapter

        //Bind the recyclerview and Add a LayoutManager
        mBinding.recyclerBlogList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        mBinding.recyclerBlogList.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                if (!isLoading) {
                    loadMoreSearchData()
                }
            }
        })
        return mBinding.root
    }

    private fun loadMoreSearchData() {
        isLoading = true
        pageno += 1
        blogAdapter?.addLoadingView()
        Handler().postDelayed({
            //Do something after 2 sec
            observeViewModel(viewModel)
        }, 2000)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BlogListViewModel::class.java)
        observeViewModel(viewModel)
    }

    // get data from web service and update UI
    private fun observeViewModel(viewModel: BlogListViewModel) {
        // Update the list when the data changes
        viewModel.getBlogListObservable(activity!!, Injector.provideCache(activity!!), pageno)
            .observe(viewLifecycleOwner, Observer<Resource<MutableList<Blog>>> {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            isLoading = false
                            blogAdapter?.removeLoadingView()
                            resource.message?.let { msg ->
                                pageno = 1
                                Snackbar.make(
                                    activity?.window?.decorView?.rootView!!,
                                    msg,
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                            resource.data?.let { blogs -> updateUIData(blogs) }
                        }
                        Status.ERROR -> {
                            isLoading = false
                            blogAdapter?.removeLoadingView()
                            resource.message?.let { msg ->
                                Snackbar.make(
                                    activity?.window?.decorView?.rootView!!,
                                    msg,
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        Status.LOADING -> {
                        }
                    }
                }
            })
    }


    private fun updateUIData(bList: MutableList<Blog>) {
        //mBinding.recyclerBlogList.setItemViewCacheSize(blogList.size)
        if (pageno == 1) {
            blogList.clear()

        }
        blogList.addAll(bList)
        // clear list, add new items in list and refresh it using notifyDataSetChanged
        blogAdapter?.notifyDataSetChanged()
    }

}
