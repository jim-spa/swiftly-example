package com.sirisdevelopment.swiftly.data

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class SwiftlyDataInterceptor(
    private val applicationContext: Context,
    private val cacheDuration: Int) : Interceptor
{
    val context: Context

    init {
        this.context = applicationContext
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url.newBuilder()
            .build()

        val newRequest = request.newBuilder()
            .url(url)
            .addHeader("Accept", "application/json;versions=1")
            .addHeader("Cache-Control", "public, max-age=$cacheDuration")
            .build()

        return chain.proceed(newRequest)
    }
}