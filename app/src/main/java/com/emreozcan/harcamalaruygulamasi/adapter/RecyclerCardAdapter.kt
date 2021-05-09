package com.emreozcan.harcamalaruygulamasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.harcamalaruygulamasi.model.Harcamalar
import com.emreozcan.harcamalaruygulamasi.R
import com.emreozcan.harcamalaruygulamasi.databinding.CardHarcamaTasarimBinding
import com.emreozcan.harcamalaruygulamasi.databinding.FragmentGirisBinding
import kotlinx.android.synthetic.main.card_harcama_tasarim.view.*
import kotlin.math.roundToInt

/**
 * Recycler ViewBinding -> https://www.youtube.com/watch?v=vmJFXmefBsM
 */
class RecyclerCardAdapter(private val harcamaList : ArrayList<Harcamalar>, private val listener : Listener, private val currency : String) : RecyclerView.Adapter<RecyclerCardAdapter.ItemHolder>() {
    //Cardlara tıklanıldığını anlamak için oluşturulmuştur daha farklı şekillerde de kullanımını görebilirsiniz
    interface Listener{
        fun cardItemClick(harcama : Harcamalar, currency: String)
    }

    class ItemHolder(val binding: CardHarcamaTasarimBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(CardHarcamaTasarimBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val harcama = harcamaList[position]
        holder.binding.textViewCardAciklama.text = harcama.aciklama
        holder.binding.textViewCardHarcama.text = "${harcama.harcama?.roundToInt()} $currency"

        //Gelen sınıfın kategorisine göre resmin değişmesi
        if (harcama.kategori=="Fatura"){
            holder.binding.imageView.setImageResource(R.drawable.ic_fatura)
        }else if (harcama.kategori == "Kira"){
            holder.binding.imageView.setImageResource(R.drawable.ic_kira)
        }else{
            holder.binding.imageView.setImageResource(R.drawable.ic_diger)
        }
        holder.binding.cardView.setOnClickListener{listener.cardItemClick(harcama,currency)}
    }

    override fun getItemCount(): Int {
        return harcamaList.size
    }
}