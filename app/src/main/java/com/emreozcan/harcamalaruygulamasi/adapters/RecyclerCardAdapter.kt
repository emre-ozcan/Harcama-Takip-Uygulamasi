package com.emreozcan.harcamalaruygulamasi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.harcamalaruygulamasi.data.database.entity.HarcamalarEntity
import com.emreozcan.harcamalaruygulamasi.databinding.CardHarcamaTasarimBinding
import com.emreozcan.harcamalaruygulamasi.ui.fragments.home.GirisFragment

class RecyclerCardAdapter: RecyclerView.Adapter<RecyclerCardAdapter.MyViewHolder>() {

    private var expenseList = emptyList<HarcamalarEntity>()
    private var rate = 1.0
    private var currency = "₺"

    class MyViewHolder(private val binding: CardHarcamaTasarimBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(harcama: HarcamalarEntity,rate: Double,currency: String){
            binding.harcama = harcama
            binding.currency = currency
            binding.rate = rate
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CardHarcamaTasarimBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(expenseList[position],rate,currency)
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    fun setData(data: List<HarcamalarEntity>,rate: Double = 1.0,currency: String = "₺"){
        this.rate = rate
        this.currency = currency
        expenseList = data
        notifyDataSetChanged()

    }
}