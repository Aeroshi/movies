package com.aeroshi.movies.model


import com.aeroshi.movies.model.service.IMovieService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object NetworkAPI {

    fun getMovieService(): IMovieService =
        getRetrofitInstance().create(IMovieService::class.java)

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://api.nytimes.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient())
            .build()
    }

    private fun okHttpClient(): OkHttpClient {
        val timeout: Long = 30
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(getInterceptor())
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .callTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .build()
    }


    private fun getInterceptor(): Interceptor {
        return Interceptor { chain ->
            val closeConnectionRequest = chain
                .request()
                .newBuilder()
                .addHeader("Connection", "close")
                .build()
            chain.proceed(closeConnectionRequest)
        }
    }

}