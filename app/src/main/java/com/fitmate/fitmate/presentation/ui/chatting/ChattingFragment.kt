package com.fitmate.fitmate.presentation.ui.chatting

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentChattingBinding
import com.fitmate.fitmate.domain.model.ChatItem
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.ChatAdapter
import com.fitmate.fitmate.util.HeightProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.time.Instant
import javax.inject.Inject

@AndroidEntryPoint
class ChattingFragment : Fragment(R.layout.fragment_chatting) {

    private lateinit var binding: FragmentChattingBinding
    private lateinit var heightProvider: HeightProvider
    private var webSocket: WebSocket? = null
    @Inject lateinit var dbChatUseCase: DBChatUseCase
    private val fitGroupId = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment(view)
        setupClickListeners()
        initHeightProvider()
        setUpRecyclerView()
        setupWebSocketConnection("$fitGroupId")

        binding.ImageViewChattingSendMySpeech.setOnClickListener {
            val message = binding.editTextChattingMySpeech.text.toString()
            if (message.isNotEmpty()) {
                    sendChatMessage(message)
            }
        }
    }

    private fun initFragment(view: View) {
        binding = FragmentChattingBinding.bind(view)
        binding.containerExtraFunction.layoutTransition = null
        binding.toolbarFragmentChatting.setupWithNavController(findNavController())
    }

    private fun setUpRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerViewFragmentChatting
        val adapter = ChatAdapter()
        val testItems = mutableListOf<ChatItem>()
        testItems.add(ChatItem(0, "$fitGroupId", "경원", "안녕"))
        testItems.add(ChatItem(1, "$fitGroupId", "현구", "응 그래"))
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null
        adapter.submitList(testItems.toList())
        adapter.submitList(testItems)
    }

    private fun setupClickListeners() {
        binding.imageViewChattingToolbarForDrawerLayout.setOnClickListener { toggleDrawer() }

        listOf(
            binding.buttonFragmentChattingFitMateProgress to R.id.action_chattingFragment_to_groupProgressFragment,
            binding.buttonFragmentChattingVote to R.id.action_chattingFragment_to_groupVoteFragment,
            binding.buttonFragmentChattingFine to R.id.action_chattingFragment_to_groupFineFragment,
            binding.buttonFragmentChattingFitOff to R.id.action_chattingFragment_to_groupFitOffFragment,
            binding.buttonFragmentChattingCertification to R.id.certificateFragment2
        ).forEach { pair ->
            pair.first.setOnClickListener {
                hideKeyboard()
                findNavController().navigate(pair.second)
            }
        }

        binding.buttonFragmentChattingTransfer.setOnClickListener {
            hideKeyboard()
            Toast.makeText(context, "Group 계좌가 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.imageViewChattingFragmentOpenContentList.setOnClickListener {
            toggleExtraFunctionContainer()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayoutForFragmentChatting.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayoutForFragmentChatting.closeDrawer(GravityCompat.END)
        } else {
            binding.drawerLayoutForFragmentChatting.openDrawer(GravityCompat.END)
        }
    }

    private fun toggleExtraFunctionContainer() {
        binding.containerExtraFunction.visibility = if (binding.containerExtraFunction.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        if (binding.containerExtraFunction.visibility == View.VISIBLE) hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun initHeightProvider() {
        activity?.let {
            heightProvider = HeightProvider(it).init().setHeightListener { height ->
                if (height > 0) {
                    binding.containerExtraFunction.visibility = View.GONE
                    binding.containerExtraFunction.layoutParams.height = height
                }
            }
        }
    }

    private fun setupWebSocketConnection(fitGroupId: String) {
        val client = OkHttpClient()
        val request = if(isEmulator()) {
            Request.Builder().url("ws://10.0.0.2:8080/ws/:${fitGroupId}").build()
        } else {
            Request.Builder().url("ws://localhost:8080/ws/:${fitGroupId}").build()
        }

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("woojugoing_websocket", "WebSocket 연결 성공, ${request.url()}")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("woojugoing_websocket", "실패, ${t.message}, ${request.url()}, ${Emulator()}")
            }
        })
    }

    private fun sendMessage(message: String) {
        val jsonObject = JSONObject().apply {
            put("id", generateMessageId())
            put("message", message)
        }
        val boolean = webSocket?.send(jsonObject.toString())
        Log.d("woojugoing_send_json", jsonObject.toString())
        Log.d("woojugoing_send_?", boolean.toString())
    }

    private fun sendChatMessage(message: String) {
        val newChatItem = ChatItem(null, "testGroupId", "경원", message)
        sendMessage(message)
        lifecycleScope.launch { dbChatUseCase.insert(newChatItem) }
        binding.editTextChattingMySpeech.setText("")
        val testItems = (binding.recyclerViewFragmentChatting.adapter as? ChatAdapter)?.currentList?.toMutableList()
        testItems?.add(newChatItem)
        (binding.recyclerViewFragmentChatting.adapter as? ChatAdapter)?.submitList(testItems)
        binding.recyclerViewFragmentChatting.post {
            binding.recyclerViewFragmentChatting.smoothScrollToPosition(testItems?.size ?: (0 - 1))
        }
    }

    private fun generateMessageId(): String {
        return "${Instant.now()}${"fitGroupId"}${"fitMateId"}"
    }

    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("Android/sdk_gphone_")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for arm64")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
    }

    fun Emulator(): String {
        return Build.MODEL
    }
}
