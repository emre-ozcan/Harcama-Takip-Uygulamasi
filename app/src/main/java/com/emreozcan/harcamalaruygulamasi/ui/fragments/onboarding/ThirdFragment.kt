package com.emreozcan.harcamalaruygulamasi.ui.fragments.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.emreozcan.harcamalaruygulamasi.R

import com.emreozcan.harcamalaruygulamasi.databinding.FragmentThirdBinding
import com.emreozcan.harcamalaruygulamasi.viewmodels.MainViewModel

/**
 * Bu Fragment Onboarding Screen için oluşturulmuştur
 */



class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentThirdBinding.inflate(inflater,container,false)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        binding.buttonThird.setOnClickListener {
            mainViewModel.saveOnboarding(true)
            findNavController().navigate(R.id.action_thirdFragment_to_girisFragment)
        }




        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}