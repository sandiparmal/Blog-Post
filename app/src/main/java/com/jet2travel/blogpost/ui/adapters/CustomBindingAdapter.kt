package com.jet2travel.blogpost.ui.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.jet2travel.blogpost.R
import com.squareup.picasso.Picasso

class CustomBindingAdapter {
    companion object {
        // static binding adapter to load url image using picasso
        @BindingAdapter("android:imageHref")
        @JvmStatic
        fun loadImage(factImageView: ImageView, imageHref: String?) {
            if (imageHref != "") {
                Picasso.get()
                    .load(imageHref)
                    .placeholder(R.drawable.no_image_placeholder)
                    .into(factImageView)
            }
        }
    }
}