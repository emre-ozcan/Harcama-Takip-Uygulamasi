package com.emreozcan.harcamalaruygulamasi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.emreozcan.harcamalaruygulamasi.*
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentDetayBinding
import com.emreozcan.harcamalaruygulamasi.model.Harcamalar
import com.emreozcan.harcamalaruygulamasi.db.Veritabani
import kotlinx.android.synthetic.main.fragment_detay.*
import kotlin.math.roundToInt

/**
 * ViewBinding-> https://developer.android.com/topic/libraries/view-binding
 */

class DetayFragment : Fragment() {
    lateinit var harcama : Harcamalar
    lateinit var currency : String

    private var _binding: FragmentDetayBinding? = null
    private val binding get()= _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            harcama = DetayFragmentArgs.fromBundle(it).harcama
            currency = DetayFragmentArgs.fromBundle(it).currency
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetayBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Veritabani.ornegiGetir(requireContext())

        binding.textViewDetayAciklama.text = harcama.aciklama

        binding.textViewDetayHarcama.text = "${harcama.harcama?.roundToInt()} $currency"
        //Round fonksiyonu, gelen double veriyi integera çevirmek için kullanılır

        if (harcama.kategori=="Fatura"){
            binding.imageViewDetay.setImageResource(R.drawable.ic_fatura)
        }else if (harcama.kategori == "Kira"){
            binding.imageViewDetay.setImageResource(R.drawable.ic_kira)
        }else{
            binding.imageViewDetay.setImageResource(R.drawable.ic_diger)
        }

        binding.buttonSil.setOnClickListener(View.OnClickListener {
            db.harcamalarDao.delete(harcama)
            Toast.makeText(context,"Silindi ! ",Toast.LENGTH_LONG).show()
            val action = DetayFragmentDirections.actionDetayFragmentToGirisFragment()
            view.findNavController().navigate(action)
        })

        binding.buttonGeri.setOnClickListener(View.OnClickListener {
            activity?.onBackPressed()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}