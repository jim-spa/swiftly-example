package com.sirisdevelopment.swiftly.viewmodel

import com.sirisdevelopment.swiftly.SwiftlyExample
import com.sirisdevelopment.swiftly.data.SwiftlyGrocerAPI
import com.sirisdevelopment.swiftly.data.SwiftlyRepository
import com.sirisdevelopment.swiftly.data.SwiftlyServiceGenerator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import timber.log.Timber

class MainViewModel {

    val context by lazy {
        SwiftlyExample.context
    }

    fun getSwiftlyGrocerManagersSpecialsAsync() = GlobalScope.async {
        Timber.d("getSwiftlyGrocerManagersSpecials: started...")
        val swiftlyRepository = SwiftlyRepository.instance.getInstance(context).swiftlyGrocerApi.retrieveSwiftlyGroverManagerSpecials()

        Timber.d("getSwiftlyGrocerManagersSpecials: retrieveSwiftlyGroverManagerSpecials started..")
    }
}