package com.emreozcan.harcamalaruygulamasi.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.emreozcan.harcamalaruygulamasi.R
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentIsimBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_isim.*
import kotlinx.android.synthetic.main.fragment_isim.view.*


class IsimFragment : Fragment() {

    private var _binding: FragmentIsimBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIsimBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context?.getSharedPreferences("com.emreozcan.harcamalaruygulamasi",Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        val isim = sharedPreferences?.getString("isim","yok")
        val cinsiyet = sharedPreferences?.getString("cinsiyet","yok")
        /**
         * Kullanıcı eğer daha önceden isim ve cinsiyet kaydettiyse bu veriler alınır ve ekran açıldığında
         * direk gösterilir
         */

        if (isim!="yok"){
            binding.editTextIsim.setText(isim)
        }
        if (cinsiyet!="yok"){
            if (cinsiyet=="Erkek"){
                view.radioErkek.isChecked = true
            }else if (cinsiyet=="Kadın"){
                view.radioKadın.isChecked = true
            }else{
                view.radioButtonNone.isChecked = true
            }
        }
        binding.buttonKaydet.setOnClickListener(View.OnClickListener {
            val textIsim = binding.editTextIsim.text.toString();
            if (textIsim.length==0){
                Snackbar.make(view,"İsminizi Giriniz !",Snackbar.LENGTH_LONG).setAction("Tamam",
                    View.OnClickListener {  }).show()
                return@OnClickListener
            }
            
            editor?.putString("isim",editTextIsim.text.toString())
            if (view.radioErkek.isChecked){
                editor?.putString("cinsiyet","Erkek")
            }else if(view.radioKadın.isChecked){
                editor?.putString("cinsiyet","Kadın")
            }else if(view.radioButtonNone.isChecked){
                editor?.putString("cinsiyet","none")
            }else{
                Snackbar.make(view,"Cinsiyeti Seçiniz !", Snackbar.LENGTH_LONG).setAction("Tamam",
                    View.OnClickListener {  }).show()
                return@OnClickListener
            }
            editor?.apply()
            Toast.makeText(context,"Kaydedildi",Toast.LENGTH_LONG).show()

            //Android Studio 4.2 güncellemesinden sonra bu kısımda hata var gibi gösteriyor fakat hatasız şekilde çalışmaktadır
            val action = IsimFragmentDirections.actionİsimFragmentToGirisFragment()//Sorun yoktur çalıştırabilirsiniz
            Navigation.findNavController(view).navigate(action)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}