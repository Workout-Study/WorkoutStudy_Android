package com.fitmate.fitmate.presentation.ui.mygroup.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemBankListGridBinding
import com.fitmate.fitmate.domain.model.Bank

class BankListAdapter(var list: List<Bank>, val onClick:(Bank) -> Unit ): RecyclerView.Adapter<BankListAdapter.GridAdapter>(){

    class GridAdapter(val binding:ItemBankListGridBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridAdapter {
        return GridAdapter(ItemBankListGridBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GridAdapter, position: Int) {
        holder.binding.textViewBangName.text = list[position].key
        holder.binding.root.setOnClickListener {
            onClick(list[position])
        }
    }
}