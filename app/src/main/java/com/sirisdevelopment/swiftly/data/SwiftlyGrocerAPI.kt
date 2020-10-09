package com.sirisdevelopment.swiftly.data

import com.sirisdevelopment.swiftly.data.objects.SwiftlyGrocerManagersSpecialDataPacket
import retrofit2.Call
import retrofit2.http.GET

interface SwiftlyGrocerAPI {
    @GET("backup")
    fun retrieveSwiftlyGroverManagerSpecials(): Call<SwiftlyGrocerManagersSpecialDataPacket>
}