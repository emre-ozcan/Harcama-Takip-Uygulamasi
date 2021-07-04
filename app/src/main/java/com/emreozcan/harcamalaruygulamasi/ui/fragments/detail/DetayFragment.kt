package com.emreozcan.harcamalaruygulamasi.ui.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.emreozcan.harcamalaruygulamasi.R
import com.emreozcan.harcamalaruygulamasi.data.database.entity.HarcamalarEntity
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentDetayBinding
import com.emreozcan.harcamalaruygulamasi.util.deleteToast
import com.emreozcan.harcamalaruygulamasi.util.loadImageFromKategori
import com.emreozcan.harcamalaruygulamasi.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode

@AndroidEntryPoint
class DetayFragment : DialogFragment() {

    private val args by navArgs<DetayFragmentArgs>()
    private lateinit var mainViewModel: MainViewModel

    private var _binding: FragmentDetayBinding? = null
    private val binding get() = _binding!!

    private lateinit var harcama: HarcamalarEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogBackground)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        harcama = args.harcama

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetayBinding.inflate(layoutInflater,container,false)


        binding.imageViewDetay.loadImageFromKategori(harcama.kategori!!)
        binding.textViewDetayAciklama.text = harcama.aciklama
        val detayHarcama : Int = (harcama.harcama!!/args.rate).toInt()
        if (detayHarcama>1){
            binding.textViewDetayHarcama.text = "${detayHarcama} ${args.currency} "
        }else{
            val smallExpense = harcama.harcama!! / args.rate
            val rounded = smallExpense.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
            binding.textViewDetayHarcama.text = "${rounded} ${args.currency} "
        }


        binding.buttonExit.setOnClickListener {
            dialog?.dismiss()
        }

        binding.buttonDelete.setOnClickListener {
            mainViewModel.deleteExpence(harcama)
            deleteToast(requireActivity(),requireContext())
            dialog?.dismiss()
        }

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}