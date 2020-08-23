package com.jet2travel.blogpost.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jet2travel.blogpost.R
import com.jet2travel.blogpost.extensions.addFragment
import com.jet2travel.blogpost.ui.fragment.BlogFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listFragment = BlogFragment()
        // Added Fragment
        addFragment(listFragment, R.id.fragment_container)
    }
}
