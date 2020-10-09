package com.sirisdevelopment.swiftly.models

import android.content.Context
import com.sirisdevelopment.swiftly.data.ManagersSpecialListData
import com.sirisdevelopment.swiftly.data.SwiftlyRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber

class ManagersSpecialDataModel(context: Context) {
    var swiftlyRepository = SwiftlyRepository.instance.getInstance(context)
    var grocerManagersSpecialItemData = swiftlyRepository.managersSpecialListData
        set(value) {
            field = value
            grocerManagersSpecialDataObservableData.onNext(value)
        }

    var grocerManagersSpecialDataObservableData: Subject<ManagersSpecialListData> = PublishSubject.create()
    private val subscriptions = CompositeDisposable()

    init {
        subscriptions.add(swiftlyRepository.grocerManagersSpecialDataObservableData.subscribe() {
            value -> grocerManagersSpecialItemData = value
            Timber.d("ManagersSpecialDataModel managersSpecialListData list updated")
        })
    }
}