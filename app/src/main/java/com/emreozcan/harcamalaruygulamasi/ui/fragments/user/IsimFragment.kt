package com.emreozcan.harcamalaruygulamasi.ui.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.emreozcan.harcamalaruygulamasi.R
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentIsimBinding
import com.emreozcan.harcamalaruygulamasi.util.observeOnce
import com.emreozcan.harcamalaruygulamasi.util.successUserToast
import com.emreozcan.harcamalaruygulamasi.util.warningToast
import com.emreozcan.harcamalaruygulamasi.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class IsimFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentIsimBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    private  var gender = "none"
    private  var name = "none"
    private var genderChipId = 0
    private var currentGender = "Belirtmek İstemiyorum"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIsimBinding.inflate(inflater,container,false)


        observeItems()

        binding.chipGroupGender.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            gender = chip.text.toString()
            genderChipId = checkedId
            currentGender = gender
        }

        binding.buttonKaydet.setOnClickListener {
            name = binding.editTextIsim.text.toString().trim()
            if (name.isNotEmpty()){
                mainViewModel.saveNameAndGender(name,currentGender,genderChipId)
                successUserToast(requireActivity(),requireContext())
                findNavController().navigate(R.id.nameToHomeFragment)
            }else{
                warningToast(requireActivity(),requireContext())
            }

        }

        binding.buttonNameExit.setOnClickListener {
            dialog?.dismiss()
        }


        return binding.root
    }
    private fun observeItems(){

        mainViewModel.readNameAndGender.observeOnce(viewLifecycleOwner,{ genderAndName ->
            if (genderAndName.genderId != 0){
                genderChipId = genderAndName.genderId
                updateChip(genderChipId,binding.chipGroupGender)
            }
            if (genderAndName.name != "none"){
                name = genderAndName.name
                binding.editTextIsim.setText(name)
            }


        })
    }


    private fun updateChip(selectedGenderId: Int,chipGroup: ChipGroup){
        if (selectedGenderId != 0){
            try {
                val targetView = chipGroup.findViewById<Chip>(selectedGenderId)
                targetView.isChecked = true
                currentGender = targetView.text.toString()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}