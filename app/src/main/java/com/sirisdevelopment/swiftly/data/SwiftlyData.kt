package com.sirisdevelopment.swiftly.data

import android.content.Context
import android.net.ConnectivityManager
import com.sirisdevelopment.swiftly.BuildConfig
import com.sirisdevelopment.swiftly.SwiftlyExample
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class SwiftlyData(context: Context) {

    var context: Context

    init {
        this.context = context
    }

    fun isOnline(): Boolean {
        val cm =
            SwiftlyExample.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    fun createClient(context: Context, useCache : Boolean): OkHttpClient? {
        val okHttpClientBuilder = OkHttpClient.Builder()
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        okHttpClientBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        val hostnameVerifier = HostnameVerifier {_, _ -> true }
        okHttpClientBuilder.hostnameVerifier(hostnameVerifier)

        if (useCache) {
            val httpCacheDirectory = File(context.getCacheDir(), "cache_file")
            val cache = Cache(httpCacheDirectory, 20 * 1024 * 1024)
            okHttpClientBuilder
                .cache(cache)
                .addInterceptor(
                    Interceptor {
                        val originalRequest: Request = it.request()
                        val cacheHeaderValue =
                            if (isOnline()) "public, max-age=2419200" else "public, only-if-cached, max-stale=2419200"
                        val request: Request = originalRequest.newBuilder().build()
                        val response: Response = it.proceed(request)
                        response.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", cacheHeaderValue)
                            .build()
                    }
                )
                .addInterceptor(
                    Interceptor {
                        val originalRequest: Request = it.request()
                        val cacheHeaderValue =
                            if (isOnline()) "public, max-age=2419200" else "public, only-if-cached, max-stale=2419200"
                        val request: Request = originalRequest.newBuilder().build()
                        val response: Response = it.proceed(request)
                        response.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", cacheHeaderValue)
                            .build()

                    }
                )
        }

        okHttpClientBuilder.addInterceptor(SwiftlyDataInterceptor(context,60))

        okHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })

        var okHttpClient = okHttpClientBuilder.build()
        return okHttpClient
    }
}
