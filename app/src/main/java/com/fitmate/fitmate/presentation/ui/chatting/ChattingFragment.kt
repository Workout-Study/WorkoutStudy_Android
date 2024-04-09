package com.fitmate.fitmate.presentation.ui.chatting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.fitmate.fitmate.presentation.viewmodel.ChattingViewModel
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
import java.time.format.DateTimeFormatterBuilder
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class ChattingFragment : Fragment(R.layout.fragment_chatting) {

    companion object {
        const val chatServerAddress = BuildConfig.CHAT_SERVER_ADDRESS
    }

    private lateinit var binding: FragmentChattingBinding
    private lateinit var heightProvider: HeightProvider
    @Inject lateinit var dbChatUseCase: DBChatUseCase
    private var webSocket: WebSocket? = null
    private var fitGroupId: Int = -1
    private var fitMateId: Int = -1
    private val viewModel: ChattingViewModel by viewModels()

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
        observeChatResponse()

        binding.ImageViewChattingSendMySpeech.setOnClickListener {
            val message = binding.editTextChattingMySpeech.text.toString()
            if (message.isNotEmpty()) {
                sendChatMessage(message)
            }
        }
    }

    private fun observeChatResponse() {
        viewModel.retrieveMessage("67d39a6f-238f-4d84-a710-e712889299f0", 1, 1, "2024-04-09 14:07:39.019121", "CHATTING")
        lifecycleScope.launch {
            viewModel.chatResponse.collect { chatResponse ->
                chatResponse?.let { response ->
                    Log.d("woojugoing_chat_log", response.messages.toString())
                    val chatItems = response.messages.map { message ->
                        ChatItem(
                            messageId = message.messageId,
                            fitGroupId = message.fitGroupId,
                            fitMateId = message.fitMateId,
                            message = message.message,
                            messageTime = LocalDateTime.parse(message.messageTime, DateTimeFormatterBuilder()
                                .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                                .appendPattern("[.SSSSSS][.SSSSS]") // 밀리초 부분을 선택적으로 파싱
                                .appendPattern("'Z'")
                                .toFormatter()),
                            messageType = message.messageType
                        )
                    }.reversed()
                    chatItems.forEach { chatItem ->
                        dbChatUseCase.insert(chatItem)
                    }
                    val adapter = ChatAdapter()
                    adapter.setCurrentUserFitMateId(fitMateId)
                    adapter.submitList(chatItems)
                    binding.recyclerViewFragmentChatting.adapter = adapter
                }
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
        binding.recyclerViewFragmentChatting.adapter = adapter
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
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
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

}