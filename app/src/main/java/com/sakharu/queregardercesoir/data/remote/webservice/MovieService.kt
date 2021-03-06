package com.sakharu.queregardercesoir.data.remote.webservice

import com.sakharu.queregardercesoir.data.locale.model.Movie
import com.sakharu.queregardercesoir.data.remote.modelResponse.ResponseMovieDetail
import com.sakharu.queregardercesoir.data.remote.modelResponse.ResponseResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService
{
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") api_key:String = API_KEY,
                                  @Query("language") language:String = "fr-FR",
                                  @Query("page") page:Int = 1) : ResponseResult<Movie>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("api_key") api_key:String = API_KEY,
                                    @Query("language") language:String = "fr-FR",
                                    @Query("page") page:Int = 1) : ResponseResult<Movie>

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(@Query("api_key") api_key:String = API_KEY,
                                  @Query("language") language:String = "fr-FR",
                                  @Query("page") page:Int = 1) : ResponseResult<Movie>

    @GET("movie/{id}")
    suspend fun getMovieDetail(@Path("id")id:Long,
                               @Query("api_key") api_key:String = API_KEY,
                               @Query("language") language:String = "fr-FR"): ResponseMovieDetail

    @GET("search/movie")
    suspend fun searchMovieFromQuery(@Query("api_key") api_key:String = API_KEY,
                                     @Query("language") language:String = "fr-FR",
                                     @Query("region") region:String = "FR",
                                     @Query("page") page:Int = 1,
                                     @Query("query") query:String) : ResponseResult<Movie>

    @GET("discover/movie")
    suspend fun searchMovieFromCharacs(@Query("api_key") api_key:String = API_KEY,
                                       @Query("language") language:String = "fr-FR",
                                       @Query("region") region:String = "FR",
                                       @Query("page") page:Int = 1,
                                       @Query("sort_by") sortBy:String?=null,
                                       @Query("release_date.gte") releaseDateGte:String?=null,
                                       @Query("release_date.lte") releaseDatelte:String?=null,
                                       @Query("vote_average.gte") voteAverageGte:Double?=null,
                                       @Query("year") year:Int?=null,
                                       @Query("certification_country") certificationCountry:String?=null,
                                       @Query("certification") certification:String?=null,
                                       @Query("with_genres") withGenres:String?=null,
                                       @Query("vote_count.gte") voteCountGte:Int=10
                                       ) : ResponseResult<Movie>

    @GET("discover/movie")
    suspend fun searchForSuggestedMovies(@Query("api_key") api_key:String = API_KEY,
                                       @Query("language") language:String = "fr-FR",
                                       @Query("region") region:String = "FR",
                                       @Query("page") page:Int = 1,
                                       @Query("sort_by") sortBy:String?="vote_average.desc",
                                       @Query("with_genres") withGenres:String?=null,
                                       @Query("vote_count.gte") voteCountGte:Int=10
    ) : ResponseResult<Movie>

    @GET("movie/{id}/similar")
    suspend fun getSimilarMoviesFromMovieId(@Path("id")id:Long,
                                  @Query("api_key") api_key:String = API_KEY,
                                  @Query("language") language:String = "fr-FR",
                                  @Query("page") page:Int = 1) : ResponseResult<Movie>


    companion object
    {
        private const val BASE_ADRESS="https://api.themoviedb.org/3/"
        const val API_KEY : String ="378e894543dbaa8f54acecd7e63168b1"
        const val IMAGE_PREFIX_POSTER : String ="https://image.tmdb.org/t/p/w185/"
        const val IMAGE_PREFIX_POSTER_HIRES : String ="https://image.tmdb.org/t/p/w780/"
        const val IMAGE_PREFIX_BACKDROP : String ="https://image.tmdb.org/t/p/w780/"
        const val IMAGE_PREFIX_BACKDROP_HIRES : String ="https://image.tmdb.org/t/p/w1280/"
        const val NUMBER_MOVIES_RETRIEVE_BY_REQUEST = 20

        fun create(): MovieService
        {
            /* PERMET DE VERIFIER LE CONTENU DU JSON */
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_ADRESS)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(MovieService::class.java)
        }
    }
}