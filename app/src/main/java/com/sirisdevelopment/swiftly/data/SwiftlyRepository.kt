package com.sirisdevelopment.swiftly.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sirisdevelopment.swiftly.data.objects.SwiftlyGrocerManagersSpecialDataPacket
import com.sirisdevelopment.swiftly.data.objects.SwiftlyManagerSpecialItemData
import com.sirisdevelopment.swiftly.util.SingletonHolder
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalStateException

class SwiftlyRepository private constructor(context: Context) : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    companion object {
        var instance: SingletonHolder<SwiftlyRepository, Context> =
            SingletonHolder(::SwiftlyRepository)
        private const val PRIVATE_MODE = 0
        private const val PREF_NAME = "swiftly_example"

        private const val MANAGERS_SPECIALS = "managers_specials"
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    }

    var sharedPrefs: SharedPreferences

    val swiftlyGrocerApi: SwiftlyGrocerAPI

    var managersSpecialListData: ManagersSpecialListData

    var grocerManagersSpecialDataObservableData: Subject<ManagersSpecialListData> = PublishSubject.create()

    init {
        sharedPrefs = context.getSharedPreferences(
            PREF_NAME,
            PRIVATE_MODE
        )

        swiftlyGrocerApi =
            SwiftlyServiceGenerator().createService(context, SwiftlyGrocerAPI::class.java)

        val json = sharedPrefs.getString(MANAGERS_SPECIALS, "")
        try {
            managersSpecialListData =
                gson.fromJson(json, SwiftlyGrocerManagersSpecialDataPacket::class.java)
        } catch (e: IllegalStateException) {
            managersSpecialListData = SwiftlyGrocerManagersSpecialDataPacket()
        }
        Timber.d("SwiftlyRepository managersSpecialListData list updated")
        grocerManagersSpecialDataObservableData.onNext(managersSpecialListData)

        launch {
            val call = swiftlyGrocerApi.retrieveSwiftlyGroverManagerSpecials()
            swiftlyGrocerApi.retrieveSwiftlyGroverManagerSpecials()
            var swiftlyGrocerDataPacket = call.execute().body()
            Timber.d(
                "init: swiftlyGrocerDataPacket[%s]",
                gson.toJson(swiftlyGrocerDataPacket)
            )
            saveSwiftlyGrocerManagerSpecialsData(swiftlyGrocerDataPacket)
        }

    }

    fun saveSwiftlyGrocerManagerSpecialsData(swiftlyGrocerManagersSpecialDataPacket: SwiftlyGrocerManagersSpecialDataPacket?) {
        sharedPrefs.edit()
            .putString(MANAGERS_SPECIALS, gson.toJson(swiftlyGrocerManagersSpecialDataPacket))
            .apply()
        managersSpecialListData = swiftlyGrocerManagersSpecialDataPacket!!
        grocerManagersSpecialDataObservableData.onNext(managersSpecialListData)
    }
}