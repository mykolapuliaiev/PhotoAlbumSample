package com.example.photoalbumsample.networking

import com.example.photoalbumsample.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * ApiFactory for networking
 */
object ApiFactory {

    /**
     * Creating Interceptor without Authorization
     */
    private val interceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder().apply {
            method(original.method, original.body)
        }
        chain.proceed(request.build())
    }

    /**
     * Creating logging interceptor for logs displaying
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * OkhttpClient for building http request url
     */
    private val networkClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(interceptor)
        .addInterceptor(loggingInterceptor)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Moshi for data serialization
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Retrofit setup
     */
    fun retrofit(): Retrofit = Retrofit.Builder().apply {
        client(networkClient)
        baseUrl(Constants.BASE_URL)
        addConverterFactory(MoshiConverterFactory.create(moshi))
    }.build()
}