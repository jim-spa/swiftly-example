package com.sirisdevelopment.swiftly.data

import android.content.Context
import com.google.gson.Gson
import com.sirisdevelopment.swiftly.SwiftlyExample.Companion.context
import com.sirisdevelopment.swiftly.data.objects.SwiftlyGrocerManagersSpecialDataPacket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SwiftlyDataService(context: Context) {
    var swiftlyGrocerAPI: SwiftlyGrocerAPI = SwiftlyServiceGenerator().createService(context, SwiftlyGrocerAPI::class.java)
    var swiftlyRepository: SwiftlyRepository = SwiftlyRepository.instance.getInstance(context)

    fun getSwiftlyGrocerManagersSpecials() = GlobalScope.async {
        if (context != null) {
            val call = swiftlyGrocerAPI.retrieveSwiftlyGroverManagerSpecials()
            call.enqueue(object : Callback<SwiftlyGrocerManagersSpecialDataPacket?> {
                override fun onResponse(
                    call: Call<SwiftlyGrocerManagersSpecialDataPacket?>?,
                    response: Response<SwiftlyGrocerManagersSpecialDataPacket?>
                ) {
                    Timber.d(
                        "onResponse: response.body()[%s]",
                        response.body().toString()
                    )
                    processGetSwiftlyGrocerManagersSpecials(response.body())
                }

                override fun onFailure(call: Call<SwiftlyGrocerManagersSpecialDataPacket?>?, t: Throwable) {
                    Timber.e("onFailure: error[%s]", t.message)
                }

            })
        } else {
            Timber.d("getSwiftlyGrocerManagersSpecials: context was null")
        }
    }

    fun processGetSwiftlyGrocerManagersSpecials(swiftlyGrocerManagersSpecialDataPacket : SwiftlyGrocerManagersSpecialDataPacket?) {
        if (swiftlyGrocerManagersSpecialDataPacket == null ||
            swiftlyGrocerManagersSpecialDataPacket.managerSpecialItemDataList.isEmpty()) {
            Timber.d("processGetSwiftlyGrocerManagersSpecials: swiftlyGrocerManagersSpecialDataPacket was null[%b] or empty",
            swiftlyGrocerManagersSpecialDataPacket == null)
        } else {
            Timber.d("processGetSwiftlyGrocerManagersSpecials: swiftlyGrocerManagersSpecialDataPacket[%s]",
                swiftlyGrocerManagersSpecialDataPacket.toString())
            swiftlyRepository.saveSwiftlyGrocerManagerSpecialsData(swiftlyGrocerManagersSpecialDataPacket)
            Timber.d("processGetSwiftlyGrocerManagersSpecials: swiftlyGrocerManagersSpecialDataPacket[%s]",
                Gson().toJson(swiftlyGrocerManagersSpecialDataPacket)
            )
        }
    }
}