package com.fitmate.fitmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fitmate.fitmate.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerViewChat) as NavHostFragment
        navController = navHostFragment.navController

        // Retrieve intent extras
        val fitGroupId = intent.getIntExtra("fitGroupId", -1)
        val fitMateId = intent.getIntExtra("fitMateId", -1)

        // Create a bundle with the retrieved extras
        val bundle = Bundle().apply {
            putInt("fitGroupId", fitGroupId)
            putInt("fitMateId", fitMateId)
        }

        navController.navigate(R.id.chattingFragment, bundle)
    }
}
