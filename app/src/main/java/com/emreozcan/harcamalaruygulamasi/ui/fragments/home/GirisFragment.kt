package com.emreozcan.harcamalaruygulamasi.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.harcamalaruygulamasi.R
import com.emreozcan.harcamalaruygulamasi.adapters.RecyclerCardAdapter
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentGirisBinding
import com.emreozcan.harcamalaruygulamasi.data.database.entity.HarcamalarEntity
import com.emreozcan.harcamalaruygulamasi.util.observeOnce
import com.emreozcan.harcamalaruygulamasi.viewmodels.MainViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import java.math.RoundingMode
import kotlin.math.roundToInt

@AndroidEntryPoint
class GirisFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { RecyclerCardAdapter() }

    private var _binding: FragmentGirisBinding? = null
    private val binding get() = _binding!!

    private var dollarRate: Double? = null
    private var euroRate: Double? = null
    private var sterlinRate: Double? = null

    private var harcamaList: List<HarcamalarEntity> = emptyList()
    private lateinit var currentCurrency: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGirisBinding.inflate(inflater, container, false)
        val view = binding.root

        readDatabase()
        getRates()

        prepareRecycler()
        chipGroup()
        hideFab()


        mainViewModel.readNameAndGender.observe(viewLifecycleOwner,{ genderAndName ->
            if (genderAndName.gender != "none" && genderAndName.name != "none"){
                var gender = " "
                when(genderAndName.gender){
                    "Erkek" -> gender = "Bey"
                    "Kadın" -> gender = "Hanım"
                }
                binding.textViewIsim.text = "${genderAndName.name} $gender"
            }

        })


        binding.textViewIsim.setOnClickListener {
            findNavController().navigate(R.id.homeFragmentToName)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_girisFragment_to_ekleFragment)
        }


        mainViewModel.readCurrentCurrencyId.observeOnce(viewLifecycleOwner, {
            updateChip(it, binding.chipGroup)
        })

        observeCurrentCurrency()


        return view
    }
    private fun observeCurrentCurrency(){
        mainViewModel.readCurrentCurrency.observe(viewLifecycleOwner, { currency ->
            currentCurrency = currency
            setAdapter(currency)
        }
        )
    }
    private fun setAdapter(currency: String){
        when (currency) {
            "₺" -> mAdapter.setData(harcamaList)
            "$" -> mAdapter.setData(harcamaList, dollarRate!!, "$")
            "€" -> mAdapter.setData(harcamaList, euroRate!!, "€")
            "£" -> mAdapter.setData(harcamaList, sterlinRate!!, "£")
        }
        binding.recyclerView.scheduleLayoutAnimation()
        calculateHarcama(harcamaList, currency)
    }

    private fun prepareRecycler() {
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getRates() {
        mainViewModel.readDollar.observeOnce(viewLifecycleOwner, { rate ->
            dollarRate = rate
            if (rate == 0.0){
                MaterialAlertDialogBuilder(requireContext()).setTitle("İnternete Bağlı Değilsiniz")
                    .setMessage("Uygulamanın doğru çalışabilmesi için en az bir kere internetiniz açık olarak giriş yapmanız gerekmektedir")
                    .setPositiveButton("Tamam"){ dialog, which ->
                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    }.setCancelable(false).show()
            }
        })
        mainViewModel.readEuro.observeOnce(viewLifecycleOwner, { rate ->
            euroRate = rate
        })

        mainViewModel.readSterlin.observeOnce(viewLifecycleOwner, { rate ->
            sterlinRate = rate
        })
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readHarcamalar.observe(viewLifecycleOwner, { database ->
                if (!database.isNullOrEmpty()) {
                    showNoData(false)
                    harcamaList = database
                    binding.recyclerView.scheduleLayoutAnimation()

                    if (this@GirisFragment::currentCurrency.isInitialized){
                        setAdapter(currentCurrency)
                    }

                } else {
                    harcamaList = emptyList()
                    binding.textViewHarcama.text = "Yoktur"
                    showNoData(true)
                }
            })
        }
    }


    private fun showNoData(boolean: Boolean) {
        binding.imageViewNoData.isVisible = boolean
        binding.textViewNoData.isVisible = boolean
        binding.recyclerView.isVisible = !boolean
    }

    private fun chipGroup() {
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            when (chip.text.toString()) {
                "TL" -> {
                    mainViewModel.saveCurrentCurrency("₺")
                }
                "DOLAR" -> {
                    mainViewModel.saveCurrentCurrency("$")
                }
                "EURO" -> {
                    mainViewModel.saveCurrentCurrency("€")
                }
                "STERLIN" -> {
                    mainViewModel.saveCurrentCurrency("£")
                }
            }

            mainViewModel.saveCurrentCurrencyId(checkedId)
        }

    }

    private fun calculateHarcama(harcamaList: List<HarcamalarEntity>, currency: String) {
        var harcama = 0.0
        var rate = 1.0
        when (currency) {
            "$" -> rate = dollarRate!!
            "€" -> rate = euroRate!!
            "£" -> rate = sterlinRate!!
        }
        for (h in harcamaList) {
            harcama += h.harcama!!
        }
        harcama = harcama / rate
        if (harcama <= 0) {
            binding.textViewHarcama.text = "Yoktur"
        }else {
            val rounded = harcama.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
            binding.textViewHarcama.text = "${rounded} $currency"
        }

    }

    private fun updateChip(selectedCurrencyId: Int, chipGroup: ChipGroup) {
        if (selectedCurrencyId != 0) {
            try {
                val targetView = chipGroup.findViewById<Chip>(selectedCurrencyId)
                targetView.isChecked = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun hideFab() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0 && !binding.fab.isShown) {
                    binding.fab.show()
                } else if (dy > 0 && binding.fab.isShown) {
                    binding.fab.hide()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}