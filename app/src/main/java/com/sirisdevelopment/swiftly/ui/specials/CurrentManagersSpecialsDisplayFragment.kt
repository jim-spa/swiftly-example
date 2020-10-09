package com.sirisdevelopment.swiftly.ui.specials

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.sirisdevelopment.swiftly.R
import com.sirisdevelopment.swiftly.adapters.ManagerSpecialRecyclerAdapter
import com.sirisdevelopment.swiftly.viewmodel.ManagersSpecialsViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_current_manager_specials.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class CurrentManagersSpecialsDisplayFragment : Fragment() {

    lateinit var managersSpecialsViewModel: ManagersSpecialsViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var managerSpecialRecyclerAdapter: ManagerSpecialRecyclerAdapter
    private val subscriptions = CompositeDisposable()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView")
        return inflater.inflate(R.layout.fragment_current_manager_specials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated")
        managersSpecialsViewModel = ManagersSpecialsViewModel(activity as Context)
        linearLayoutManager = LinearLayoutManager(activity)

        managers_specials.layoutManager = linearLayoutManager
        var displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        managerSpecialRecyclerAdapter = ManagerSpecialRecyclerAdapter(managersSpecialsViewModel.managersSpecialListData, displayMetrics)
        managers_specials.adapter = managerSpecialRecyclerAdapter

        subscriptions.add(managersSpecialsViewModel.grocerManagersSpecialDataObservableData.subscribe(){
            value -> managerSpecialRecyclerAdapter.setManagersSpecialsListData(value)
            Timber.d("CurrentManagersSpecialsDisplayFragment managersSpecialListData list updated")
        })
        home_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_managers_specials_to_home))
    }
}
