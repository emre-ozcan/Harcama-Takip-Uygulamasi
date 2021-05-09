package com.emreozcan.harcamalaruygulamasi.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.emreozcan.harcamalaruygulamasi.R


class SplashFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var isShowed : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = context?.getSharedPreferences("com.emreozcan.harcamalaruygulamasi", Context.MODE_PRIVATE)!!
        isShowed = sharedPreferences?.getString("showed","no")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            var action : NavDirections
            if (isShowed=="yes"){
                action = SplashFragmentDirections.actionSplashFragmentToGirisFragment()
            }else{
                action = SplashFragmentDirections.actionSplashFragmentToFirstFragment()
            }
            view?.findNavController()?.navigate(action)
        },3000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}