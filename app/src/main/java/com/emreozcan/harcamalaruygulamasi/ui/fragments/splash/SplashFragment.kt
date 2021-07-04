package com.emreozcan.harcamalaruygulamasi.ui.fragments.splash

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.emreozcan.harcamalaruygulamasi.R
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentSplashBinding
import com.emreozcan.harcamalaruygulamasi.ui.MainActivity
import com.emreozcan.harcamalaruygulamasi.util.observeOnce
import com.emreozcan.harcamalaruygulamasi.viewmodels.MainViewModel


class SplashFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private var _binding : FragmentSplashBinding?= null
    private val binding get() = _binding!!

    private var isOnboardingShowed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater,container,false)

        mainViewModel.getCurrencies()

        mainViewModel.readOnboarding.observeOnce(viewLifecycleOwner,{ isShowed ->
            isOnboardingShowed = isShowed
        })

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (isOnboardingShowed){
                findNavController().navigate(R.id.action_splashFragment_to_girisFragment)
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_firstFragment)
            }
        }, 3000)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}