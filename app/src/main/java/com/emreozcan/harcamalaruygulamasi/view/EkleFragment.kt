package com.emreozcan.harcamalaruygulamasi.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.emreozcan.harcamalaruygulamasi.model.Harcamalar
import com.emreozcan.harcamalaruygulamasi.R
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentEkleBinding
import com.emreozcan.harcamalaruygulamasi.db.Veritabani
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_ekle.*
import kotlinx.android.synthetic.main.fragment_ekle.view.*


class EkleFragment : Fragment() {
    private lateinit var db : Veritabani

    private var _binding: FragmentEkleBinding? = null
    private val binding get()= _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Veritabani.ornegiGetir(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEkleBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sharedPreferences = context?.getSharedPreferences("com.emreozcan.harcamalaruygulamasi", Context.MODE_PRIVATE)

        /**
         * SharedPreferencesdan veri alınırken yanına ayrıca eğer hata olursa ve veriye erişemezse
         * bu değişkeni neye eşitleyeceğini söyleriz. Hata olmazsa bizim kaydettiğimiz veri gelir.
         *
         */
        val storedEuro = sharedPreferences?.getFloat("euro",9.99f)
        val storedDolar = sharedPreferences?.getFloat("dolar",8.3f)
        val storedSterlin = sharedPreferences?.getFloat("sterlin",11.57f)



        view.buttonEkle.setOnClickListener(View.OnClickListener {
            val aciklama = binding.editTextAciklama.text.toString()
            var harcama = binding.editTextHarcama.text.toString()


            if(aciklama.length==0){
                Snackbar.make(view,"Açıklama Boş Bırakılamaz !",Snackbar.LENGTH_LONG).setAction("Tamam",
                    View.OnClickListener {  }).show()
                return@OnClickListener
            }
            if (harcama.length==0){
                Snackbar.make(view,"Harcama Boş Bırakılamaz !",Snackbar.LENGTH_LONG).setAction("Tamam",
                    View.OnClickListener {  }).show()
                return@OnClickListener
            }
            var kategori =""
            var doubleHarcama = harcama.toDouble()

            if (binding.radioDiger.isChecked){
                kategori = "Diğer"
            }else if (binding.radioKira.isChecked){
                kategori = "Kira"
            }else if (binding.radioFatura.isChecked){
                kategori = "Fatura"
            }else{
                Snackbar.make(view,"Harcama Tipini Seçiniz !",Snackbar.LENGTH_LONG).setAction("Tamam",
                    View.OnClickListener {  }).show()
                return@OnClickListener
            }

            if (binding.radioButtonDolar.isChecked) {
                storedDolar.let {
                    doubleHarcama *= it!!
                }
            }
            else if (binding.radioButtonEuro.isChecked) {
                storedEuro.let {
                    doubleHarcama *= it!!
                }

            }
            else if (binding.radioButtonSterlin.isChecked) {
                storedSterlin.let {
                    doubleHarcama *= it!!
                }
            }else if(!binding.radioButtonTL.isChecked){
                Snackbar.make(view,"Para Birimini Seçiniz !",Snackbar.LENGTH_LONG).setAction("Tamam",
                        View.OnClickListener {  }).show()
                return@OnClickListener
            }
            val h1 = Harcamalar(kategori,doubleHarcama,aciklama)
            db.harcamalarDao.insertAll(h1)
            Toast.makeText(context,"Kaydedildi",Toast.LENGTH_LONG).show()
            val action = EkleFragmentDirections.actionEkleFragmentToGirisFragment()
            view.findNavController().navigate(action)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}