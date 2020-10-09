package com.sirisdevelopment.swiftly.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sirisdevelopment.swiftly.data.ManagersSpecialListData
import com.sirisdevelopment.swiftly.data.SwiftlyRepository
import com.sirisdevelopment.swiftly.models.ManagersSpecialDataModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber

class ManagersSpecialsViewModel(context: Context) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    val managersSpecialDataModel: ManagersSpecialDataModel
    var managersSpecialListData: ManagersSpecialListData
    var grocerManagersSpecialDataObservableData: Subject<ManagersSpecialListData> = PublishSubject.create()

    init {
        managersSpecialDataModel = ManagersSpecialDataModel(context)
        managersSpecialListData = SwiftlyRepository.instance.getInstance(context).managersSpecialListData
        subscriptions.add(managersSpecialDataModel.grocerManagersSpecialDataObservableData.subscribe {
            value -> managersSpecialListData = value
            grocerManagersSpecialDataObservableData.onNext(value)
            Timber.d("ManagersSpecialsViewModel managersSpecialListData list updated")
        })
    }
}