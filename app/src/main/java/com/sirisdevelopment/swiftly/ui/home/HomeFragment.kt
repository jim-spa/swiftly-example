package com.sirisdevelopment.swiftly.ui.home

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders.of
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.sirisdevelopment.swiftly.R
import com.sirisdevelopment.swiftly.viewmodel.HomeViewModel
import com.sirisdevelopment.swiftly.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import java.lang.Exception

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        NavHostFragment.findNavController(this)
        mainViewModel = MainViewModel()
        mainViewModel.getSwiftlyGrocerManagersSpecialsAsync()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        managers_specials_btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_to_managers_specials))

        Picasso.get().load("https://useswiftly.com/images/redesign/logo@2x.png").into(homeImage)
    }
}
