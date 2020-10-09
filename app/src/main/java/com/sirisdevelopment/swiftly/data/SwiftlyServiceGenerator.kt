package com.sirisdevelopment.swiftly.data

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SwiftlyServiceGenerator {
    companion object {
        const val SWIFTLY_GROCER_MANAGER_SPECIALS_ROOT_PATH: String = "https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/"
    }

    private val builder = Retrofit.Builder()
        .baseUrl(SWIFTLY_GROCER_MANAGER_SPECIALS_ROOT_PATH)
        .addConverterFactory(GsonConverterFactory.create())

    fun <S> createService(
        context: Context,
        serviceClass: Class<S>?
    ): S {
        val swiftlyData = SwiftlyData(context)

        val httpClient = swiftlyData.createClient(context,  true)
        builder.client(httpClient)
        val retrofit = builder.build()
        return retrofit.create(serviceClass)
    }
}
