package com.sakharu.queregardercesoir.data.remote.webservice

import com.sakharu.queregardercesoir.data.remote.modelResponse.ResponseListGenre
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface GenreService
{
    @GET("genre/movie/list")
    suspend fun getAllGenres(@Query("api_key") api_key:String = API_KEY,
                             @Query("language") language:String = "fr-FR") : ResponseListGenre

    companion object
    {
        private const val BASE_ADRESS="https://api.themoviedb.org/3/"
        const val API_KEY : String ="378e894543dbaa8f54acecd7e63168b1"

        fun create(): GenreService
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

            return retrofit.create(GenreService::class.java)
        }
    }
}