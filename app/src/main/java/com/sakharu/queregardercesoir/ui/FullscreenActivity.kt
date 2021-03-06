package com.sakharu.queregardercesoir.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sakharu.queregardercesoir.R
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService.Companion.IMAGE_PREFIX_BACKDROP_HIRES
import com.sakharu.queregardercesoir.data.remote.webservice.MovieService.Companion.IMAGE_PREFIX_POSTER_HIRES
import com.sakharu.queregardercesoir.ui.base.BaseActivity
import com.sakharu.queregardercesoir.util.*
import kotlinx.android.synthetic.main.activity_fullscreen.*

class FullscreenActivity : BaseActivity() {

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
                .error(R.drawable.film_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable>{

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean
                    {
                        loadingAnimationFullscreenActivity.hide()
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean
                    {
                        showDialogNetworkError()
                        loadingAnimationFullscreenActivity.hide()
                        return false
                    }
                })
                .into(fullscreenImageView)
        }
        else
        {
            Glide.with(this)
                .load(R.drawable.film_poster_placeholder)
                .placeholder(R.drawable.film_poster_placeholder)
                .fallback(R.drawable.film_poster_placeholder)
                .error(R.drawable.film_poster_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(fullscreenImageView)
            Toast.makeText(this,getString(R.string.errorRetrievingImage),Toast.LENGTH_LONG).show()
        }

        setUpActionBar(intent.getStringExtra(EXTRA_MOVIE_NAME).orEmpty())
    }
}
