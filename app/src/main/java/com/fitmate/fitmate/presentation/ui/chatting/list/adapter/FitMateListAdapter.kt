package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitmate.fitmate.R
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel

class FitMateListAdapter(private var fitMates: List<FitMate>, private var leaderID: String, private var myID: String, private var login: LoginViewModel) : RecyclerView.Adapter<FitMateListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textViewItemChattingFitMateName)
        val exportButton: Button = view.findViewById(R.id.buttonItemFitMateExport)
        val userProfile: ImageView = view.findViewById(R.id.imageViewItemChattingFitMateImage)
        val position: TextView = view.findViewById(R.id.textViewItemChattingFitmatePosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chatting_fit_mate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fitMate = fitMates[position]
//        login.getUserInfo(fitMate.fitMateUserId.toInt())
        holder.nameTextView.text = fitMate.fitMateUserNickname
        holder.exportButton.visibility = if (myID == leaderID) View.VISIBLE else View.INVISIBLE
        when(fitMate.fitMateUserId) {
            leaderID -> holder.position.text = "방장"
            myID -> holder.position.text = "나"
            else -> holder.position.text = ""
        }
//        Log.d("woojugoing_url", login.userInfo.value?.imageUrl!!)
//        Glide.with(holder.userProfile.context).load(if(login.userInfo.value?.imageUrl==null) "" else login.userInfo.value?.imageUrl).into(holder.userProfile)
    }

    override fun getItemCount() = fitMates.size

    fun updateData(newFitMates: List<FitMate>, leaderID: String, myID: String) {
        this.fitMates = newFitMates
        this.leaderID = leaderID
        this.myID = myID
        notifyDataSetChanged()
    }
}

data class FitMate(
    val fitMateId: Int,
    val fitMateUserId: String,
    val fitMateUserNickname: String,
    val fitMateUserProfileImageUrl: String?,
    val createdAt: String
)