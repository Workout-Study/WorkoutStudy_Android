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
import com.fitmate.fitmate.BuildConfig
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
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class ChattingFragment : Fragment(R.layout.fragment_chatting) {

    companion object {
        const val chatServerAddress = BuildConfig.CHAT_SERVER_ADDRESS
    }

    private lateinit var binding: FragmentChattingBinding
    private lateinit var heightProvider: HeightProvider
    private var webSocket: WebSocket? = null
    @Inject lateinit var dbChatUseCase: DBChatUseCase
    private var fitGroupId: Int = -1
    private var fitMateId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fitGroupId = it.getInt("fitGroupId", -1)
            fitMateId = it.getInt("fitMateId", -1)
        }
    }

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

    override fun onPause() {
        super.onPause()
        webSocket?.close(1000, "Fragment Paused")
    }

    private fun initFragment(view: View) {
        binding = FragmentChattingBinding.bind(view)
        binding.containerExtraFunction.layoutTransition = null
        binding.toolbarFragmentChatting.setupWithNavController(findNavController())
    }

    private fun setUpRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerViewFragmentChatting
        val adapter = ChatAdapter()
        adapter.setCurrentUserFitMateId(fitMateId)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null
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
        val request = Request.Builder().url("ws://3.38.227.26:8080/ws/${fitGroupId}").build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("woojugoing_websocket", "WebSocket 연결 성공, ${request.url}")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket Failure", "Connection failed: ${t.message}")

                // 실패 응답이 있을 경우, 상태 코드와 응답 본문 로깅
                response?.let {
                    Log.e("WebSocket Failure", "Response Code: ${it.code}, Response Body: ${it.body?.string()}")
                } ?: Log.e("WebSocket Failure", "No response received.")

                // 예외 스택 트레이스 로깅
                Log.e("WebSocket Failure", "Exception details:", t)

                // 추가적으로, 에뮬레이터에서 실행되고 있는지 확인
                if (isEmulator()) {
                    Log.e("WebSocket Failure", "Running on emulator. Model: ${Build.MODEL}")
                } else {
                    Log.e("WebSocket Failure", "Running on real device. Model: ${Build.MODEL}")
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d("WebSocket", "Receiving : $text")
            }
        })
    }

    private fun sendMessage(messageId: String, message: String, timeNow: LocalDateTime) {
        val jsonObject = JSONObject().apply {
            put("messageId", messageId)
            put("fitGroupId", fitGroupId)
            put("fitMateId", fitMateId)
            put("message", message)
            put("messageTime", timeNow)
            put("messageType", "CHATTING")
        }
        val success = webSocket?.send(jsonObject.toString())
        if (success == true) {
            Log.d("woojugoing_send_json", jsonObject.toString())
            Log.d("woojugoing_send_success", "Message sent successfully")
        } else {
            Log.e("woojugoing_send_error", "Failed to send message")
        }
    }

    private fun sendChatMessage(message: String) {
        val messageId = UUID.randomUUID().toString()
        val timeNow = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val newChatItem = ChatItem(messageId, fitGroupId, fitMateId, message, timeNow, "CHATTING")
        sendMessage(messageId, message, timeNow)
        lifecycleScope.launch { dbChatUseCase.insert(newChatItem) }
        binding.editTextChattingMySpeech.setText("")
        val testItems = (binding.recyclerViewFragmentChatting.adapter as? ChatAdapter)?.currentList?.toMutableList()
        testItems?.add(newChatItem)
        (binding.recyclerViewFragmentChatting.adapter as? ChatAdapter)?.submitList(testItems)
        binding.recyclerViewFragmentChatting.post {
            binding.recyclerViewFragmentChatting.smoothScrollToPosition(testItems?.size ?: (0 - 1))
        }
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