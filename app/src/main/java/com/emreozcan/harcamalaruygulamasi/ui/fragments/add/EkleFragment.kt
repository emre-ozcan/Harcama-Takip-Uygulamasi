package com.emreozcan.harcamalaruygulamasi.ui.fragments.add

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.emreozcan.harcamalaruygulamasi.R
import com.emreozcan.harcamalaruygulamasi.data.database.entity.HarcamalarEntity
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentEkleBinding
import com.emreozcan.harcamalaruygulamasi.data.database.Veritabani
import com.emreozcan.harcamalaruygulamasi.model.ExpenseTypes
import com.emreozcan.harcamalaruygulamasi.util.observeOnce
import com.emreozcan.harcamalaruygulamasi.util.successHarcamaToast
import com.emreozcan.harcamalaruygulamasi.util.warningToast
import com.emreozcan.harcamalaruygulamasi.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.annotation.meta.When

@AndroidEntryPoint
class EkleFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentEkleBinding? = null
    private val binding get() = _binding!!

    private var currencyType = "₺"
    private var expenseType = "Fatura"

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEkleBinding.inflate(inflater, container, false)


        binding.chipGroupType.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            expenseType = chip.text.toString()
            println(expenseType)
        }


        binding.chipGroupCurrency.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            currencyType = chip.text.toString()
        }



        binding.buttonEkle.setOnClickListener {
            val aciklama = binding.editTextAciklama.text.toString().trim()
            val harcama = binding.editTextHarcama.text.toString().trim()

            if (currencyType != "none" && expenseType != "none" && aciklama.isNotEmpty() && harcama.isNotEmpty()) {
                val expence =
                    HarcamalarEntity(
                        whichKategori(expenseType),
                        harcama.toDouble() * whichCurrency(currencyType),
                        aciklama
                    )
                successHarcamaToast(requireActivity(),requireContext())
                mainViewModel.insertExpence(expence)
                findNavController().navigate(R.id.action_ekleFragment_to_girisFragment)
            } else {
                warningToast(requireActivity(),requireContext())
            }
        }

        binding.buttonAddExit.setOnClickListener {
            dialog?.dismiss()
        }

        return binding.root
    }

    private fun whichKategori(string: String): ExpenseTypes {
        return when (string) {
            "Fatura" -> ExpenseTypes.FATURA
            "Kira" -> ExpenseTypes.KIRA
            else -> ExpenseTypes.DIGER
        }
    }

    private fun whichCurrency(string: String): Double {
        var rate = 1.0
        when (string) {
            "$" -> {
                mainViewModel.readDollar.observeOnce(viewLifecycleOwner, {
                    rate = it
                })
            }
            "€" -> {
                mainViewModel.readEuro.observeOnce(viewLifecycleOwner, {
                    rate = it
                })
            }
            "£" -> {
                mainViewModel.readSterlin.observeOnce(viewLifecycleOwner, {
                    rate = it
                })
            }
        }
        return rate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}