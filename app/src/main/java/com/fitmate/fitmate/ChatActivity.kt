package com.fitmate.fitmate

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.fitmate.fitmate.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var navController: NavController
    private val sharedPreferences: SharedPreferences by lazy {
        val masterKeyAlies = MasterKey
            .Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            applicationContext,
            FILE_NAME,
            masterKeyAlies,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

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

    fun loadUserPreference(): List<Any> {
        val userPreferences = mutableListOf<Any>()
        if (sharedPreferences.contains(KEY_ACCESS)) {
            val accessToken = sharedPreferences.getString(KEY_ACCESS, "")
            val refreshToken = sharedPreferences.getString(KEY_REFRESH, "")
            val userId = sharedPreferences.getInt(KEY_USER_ID, -1)
            val platform = sharedPreferences.getString(KEY_PLATFORM, "")
            val createdAt = sharedPreferences.getString(KEY_CREATED_AT, "")

            userPreferences.run {
                add(accessToken!!)
                add(refreshToken!!)
                add(userId)
                add(platform!!)
                add(createdAt!!)
            }
        }

        return userPreferences
    }

    companion object {
        private const val FILE_NAME= "user_preference"
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_PLATFORM = "platform"
        private const val KEY_CREATED_AT = "createdAt"
    }
}
