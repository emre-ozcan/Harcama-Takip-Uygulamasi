package com.emreozcan.harcamalaruygulamasi.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.emreozcan.harcamalaruygulamasi.data.database.entity.HarcamalarEntity
import com.emreozcan.harcamalaruygulamasi.model.ExpenseTypes
import com.emreozcan.harcamalaruygulamasi.ui.fragments.home.GirisFragmentDirections
import com.emreozcan.harcamalaruygulamasi.util.loadImageFromKategori
import java.math.RoundingMode

class CardHarcamaBinding {
    companion object {

        @BindingAdapter("loadKategoriImage")
        @JvmStatic
        fun loadKategoriImage(imageView: ImageView, expenseTypes: ExpenseTypes) {
            imageView.loadImageFromKategori(expenseTypes)
        }

        @BindingAdapter("rate", "currency", "expense", requireAll = true)
        @JvmStatic
        fun currencyText(textView: TextView, rate: Double, currency: String, expense: Double) {
            val newExpense: Int = (expense / rate).toInt()
            if (newExpense>1){
                textView.text = "${newExpense} $currency"
            }else{
                val smallExpense = expense / rate
                val rounded = smallExpense.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                textView.text = "${rounded} $currency"

            }
        }

        @BindingAdapter("harcamaClick","currencyClick","rateClick",requireAll = true)
        @JvmStatic
        fun harcamaSetOnClickListener(
            cardView: CardView,
            harcamalarEntity: HarcamalarEntity,
            currency: String,
            rate: Double
        ) {
            cardView.setOnClickListener {
                val action = GirisFragmentDirections.actionGirisFragmentToDetayFragment(
                    harcamalarEntity,
                    rate.toFloat(),
                    currency
                )
                cardView.findNavController().navigate(action)

            }
        }

    }

}