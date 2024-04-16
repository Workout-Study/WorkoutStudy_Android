package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R

class FitMateListAdapter(private var fitMates: List<FitMate>, private var isLeader: Boolean = false) : RecyclerView.Adapter<FitMateListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textViewItemChattingFitMateName)
        val exportButton: Button = view.findViewById(R.id.buttonItemFitMateExport)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chatting_fit_mate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fitMate = fitMates[position]
        holder.nameTextView.text = fitMate.fitMateUserId
        holder.exportButton.visibility = if(isLeader) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = fitMates.size

    fun updateData(newFitMates: List<FitMate>, isLeader: Boolean) {
        this.fitMates = newFitMates
        this.isLeader = isLeader
        notifyDataSetChanged()
    }
}

data class FitMate(
    val fitMateId: Int,
    val fitMateUserId: String,
    val createdAt: String
)