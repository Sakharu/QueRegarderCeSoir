package com.sakharu.queregardercesoir.data.remote

import com.sakharu.queregardercesoir.data.locale.movie.Movie
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieService
{
    @GET("authentication/token/new")
    suspend fun authenticate(@Query(value = "api_key") api_key:String = API_KEY) : ResponseAuth

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") api_key:String = API_KEY,
                                 @Query("language") language:String = "fr-FR",
                                 @Query("page") page:Int = 1) : ResponseResult<Movie>

    companion object
    {
        private const val BASE_ADRESS="https://api.themoviedb.org/3/"
        const val API_KEY : String ="378e894543dbaa8f54acecd7e63168b1"
        const val IMAGE_PREFIX : String ="https://image.tmdb.org/t/p/w185/"

        fun create(): MovieService
        {
            /* PERMET DE VERIFIER LE CONTENU DU JSON */
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_ADRESS)
                .client(client)
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(MovieService::class.java)
        }
    }
}