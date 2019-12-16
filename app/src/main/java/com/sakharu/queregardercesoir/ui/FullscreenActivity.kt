package com.sakharu.queregardercesoir.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService.Companion.IMAGE_PREFIX_BACKDROP_HIRES
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService.Companion.IMAGE_PREFIX_POSTER_HIRES
import com.sakharu.queregardercesoir.util.EXTRA_IMAGE_URL
import com.sakharu.queregardercesoir.util.EXTRA_TYPE_IMAGE
import com.sakharu.queregardercesoir.util.TYPE_POSTER
import kotlinx.android.synthetic.main.activity_fullscreen.*

class FullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        if (!intent.getStringExtra(EXTRA_IMAGE_URL).isNullOrEmpty())
        {
            val url = if (intent.getStringExtra(EXTRA_TYPE_IMAGE) == TYPE_POSTER)
                IMAGE_PREFIX_POSTER_HIRES+intent.getStringExtra(EXTRA_IMAGE_URL)
            else
                IMAGE_PREFIX_BACKDROP_HIRES+intent.getStringExtra(EXTRA_IMAGE_URL)
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(fullscreenImageView)
        }

        else
            Glide.with(this)
                .load(R.drawable.film_poster_placeholder)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(fullscreenImageView)
    }
}
