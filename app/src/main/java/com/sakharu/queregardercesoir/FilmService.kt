package com.sakharu.queregardercesoir

import com.example.myapplication.ResponseAuth
import com.example.myapplication.ResponseResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface FilmService
{
    @GET("authentication/token/new")
    suspend fun authenticate(@Query(value = "api_key") api_key:String = API_KEY) : ResponseAuth

    @GET("/movie/popular")
    suspend fun getPopularMovies(@Query("api_key") api_key:String = API_KEY,
                                 @Query("language") language:String = "fr-FR",
                                 @Query("page") page:Int = 1) : ResponseResult<Film>

    companion object
    {
        const val BASE_ADRESS="https://api.themoviedb.org/3/"
        const val API_KEY : String ="378e894543dbaa8f54acecd7e63168b1"
        var access_token :String=""

        fun create(): FilmService
        {
            /* PERMET DE VERIFIER LE CONTENU DU JSON
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            */
            val retrofit = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                //.client(client)
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(FilmService::class.java)
        }
    }
}