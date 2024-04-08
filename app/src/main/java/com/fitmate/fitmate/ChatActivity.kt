package com.fitmate.fitmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fitmate.fitmate.databinding.ActivityChatBinding
import com.fitmate.fitmate.presentation.ui.chatting.ChattingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fitGroupId = intent.getIntExtra("fitGroupId", -1)
        val fitMateId = intent.getIntExtra("fitMateId", -1)

        val bundle = Bundle().apply {
            putInt("fitGroupId", fitGroupId)
            putInt("fitMateId", fitMateId)
        }

        val fragment = ChattingFragment().apply {
            arguments = bundle
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerViewChat, fragment)
            .commit()
    }
}