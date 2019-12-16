package com.sakharu.queregardercesoir.ui.movieList.littleMovie

import android.widget.ImageView
import com.sakharu.queregardercesoir.data.locale.model.Movie

interface OnMovieClickListener
{
    fun onClickOnMovie(movie : Movie, imageView:ImageView)
}
