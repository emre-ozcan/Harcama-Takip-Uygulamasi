package com.emreozcan.harcamalaruygulamasi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.emreozcan.harcamalaruygulamasi.R

import com.emreozcan.harcamalaruygulamasi.databinding.FragmentSecondBinding
import kotlinx.android.synthetic.main.fragment_second.view.*

/**
 * Bu Fragment Onboarding Screen için oluşturulmuştur
 */
class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSecond.setOnClickListener(View.OnClickListener {
            val action = SecondFragmentDirections.actionSecondFragmentToThirdFragment()
            view.findNavController().navigate(action)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}