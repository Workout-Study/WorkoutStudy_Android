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
        loadChatMessage()
        observeChatResponse()
        scrollBottom()

        binding.ImageViewChattingSendMySpeech.setOnClickListener {
            val message = binding.editTextChattingMySpeech.text.toString()
            if (message.isNotEmpty()) {
                sendChatMessage(message)
            }
        }
    }

    private fun loadChatMessage() {
        lifecycleScope.launch {
            val chatItems = dbChatUseCase.getChatItemsByFitGroupId(fitGroupId).map { chatEntity ->
                ChatItem(
                    messageId = chatEntity.messageId,
                    fitGroupId = chatEntity.fitGroupId,
                    fitMateId = chatEntity.fitMateId,
                    message = chatEntity.message,
                    messageTime = chatEntity.messageTime,
                    messageType = chatEntity.messageType
                )
            }

            val adapter = binding.recyclerViewFragmentChatting.adapter as? ChatAdapter ?: ChatAdapter().also { binding.recyclerViewFragmentChatting.adapter = it }
            adapter.setCurrentUserFitMateId(fitMateId)
            adapter?.submitList(chatItems) {
                binding.recyclerViewFragmentChatting.post {
                    binding.recyclerViewFragmentChatting.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun observeChatResponse() {
        viewModel.retrieveMessage()
        //viewModel.retrieveMessage("369ec144-ea1b-4c38-b46e-09d46510f3d1", 1, 1, "2024-04-10 05:25:05.689639", "CHATTING")
        lifecycleScope.launch {
            viewModel.chatResponse.collect { chatResponse ->
                // chatResponse와 chatResponse.messages 모두 null이 아닌지 확인합니다.
                if (chatResponse != null && chatResponse.messages != null && chatResponse.messages.isNotEmpty()) {
                    val newItems = chatResponse.messages.filter { it.fitGroupId == fitGroupId }
                        .mapNotNull { message ->
                            message.messageTime?.let { time ->
                                ChatItem(
                                    messageId = message.messageId,
                                    fitGroupId = message.fitGroupId,
                                    fitMateId = message.fitMateId,
                                    message = message.message,
                                    messageTime = LocalDateTime.parse(time, DateTimeFormatterBuilder()
                                        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                                        .appendPattern("[.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]") // 밀리초 부분을 선택적으로 파싱
                                        .appendPattern("'Z'")
                                        .toFormatter()),
                                    messageType = message.messageType
                                )
                            }
                        }.reversed()

                    newItems.forEach { newItem ->
                        dbChatUseCase.insert(newItem)
                    }

                    loadChatMessage()

                } else {
                    Log.d("ChatFragment", "No messages to load or messages are null")
                }
            }
        }
    }

    private fun scrollBottom() {
        binding.recyclerViewFragmentChatting.post {
            val itemCount = binding.recyclerViewFragmentChatting.adapter?.itemCount ?: 0
            if (itemCount > 0) {
                binding.recyclerViewFragmentChatting.scrollToPosition(itemCount - 1)
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

                    scrollToBottom()
                }
            }
        }
    }

    private fun scrollToBottom() {
        binding.recyclerViewFragmentChatting.apply {
            post {
                if (adapter?.itemCount ?: 0 > 0) {
                    smoothScrollToPosition(adapter!!.itemCount - 1)
                }
            }
        }
    }

    private fun setupWebSocketConnection(fitGroupId: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://3.38.227.26:8080/chat?fitGroupId=${fitGroupId}&fitMateId=${fitMateId}").build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("woojugoing_websocket", "WebSocket 연결 성공, ${request.url}")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket Failure", "Connection failed: ${t.message}")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Receiving : $text")
                val messageObject = JSONObject(text)
                val receivedFitGroupId = messageObject.getInt("fitGroupId")

                // 현재 채팅방의 groupId와 받은 메시지의 groupId가 일치하는지 확인
                if (receivedFitGroupId == this@ChattingFragment.fitGroupId) {
                    val messageId = messageObject.getString("messageId")
                    val fitMateId = messageObject.getInt("fitMateId")
                    val message = messageObject.getString("message")
                    val messageTime = messageObject.getString("messageTime")
                    val messageType = messageObject.getString("messageType")

                    val parsedMessageTime = LocalDateTime.parse(messageTime, DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                        .appendPattern("[.SSSSSS][.SSSSS]") // 밀리초 부분을 선택적으로 파싱
                        .appendPattern("'Z'")
                        .toFormatter()
                        .withZone(ZoneId.systemDefault()))

                    val chatItem = ChatItem(messageId, receivedFitGroupId, fitMateId, message, parsedMessageTime, messageType)

                    // 메인 스레드에서 UI 작업 실행
                    activity?.runOnUiThread {
                        lifecycleScope.launch {
                            // DB에 메시지 추가
                            dbChatUseCase.insert(chatItem)

                            // 어댑터에 아이템 추가
                            val adapter = binding.recyclerViewFragmentChatting.adapter as? ChatAdapter
                            val currentList = adapter?.currentList?.toMutableList() ?: mutableListOf()
                            currentList.add(chatItem)
                            adapter?.submitList(currentList)

                            // 목록의 마지막으로 스크롤
                            binding.recyclerViewFragmentChatting.smoothScrollToPosition(adapter?.itemCount ?: 0 - 1)
                        }
                    }
                }
            }
        })
    }

    private fun sendMessage(messageId: String, message: String, timeNow: LocalDateTime): Boolean {
        val jsonObject = JSONObject().apply {
            put("messageId", messageId)
            put("fitGroupId", fitGroupId)
            put("fitMateId", fitMateId)
            put("message", message)
            put("messageTime", timeNow)
            put("messageType", "CHATTING")
        }
        val success = webSocket?.send(jsonObject.toString()) ?: false
        if (success) {
            Log.d("woojugoing_send_json", jsonObject.toString())
            Log.d("woojugoing_send_success", "Message sent successfully")
        } else {
            Log.e("woojugoing_send_error", "Failed to send message")
        }
        return success
    }


    private fun sendChatMessage(message: String) {
        val messageId = UUID.randomUUID().toString()
        val timeNow = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val isMessageSent = sendMessage(messageId, message, timeNow)

        if (isMessageSent) {
            val newChatItem = ChatItem(messageId, fitGroupId, fitMateId, message, timeNow, "CHATTING")
            lifecycleScope.launch { dbChatUseCase.insert(newChatItem) }

            binding.editTextChattingMySpeech.setText("")
            val testItems = (binding.recyclerViewFragmentChatting.adapter as? ChatAdapter)?.currentList?.toMutableList()
//            testItems?.add(newChatItem)
//            (binding.recyclerViewFragmentChatting.adapter as? ChatAdapter)?.submitList(testItems)
            binding.recyclerViewFragmentChatting.post {
                binding.recyclerViewFragmentChatting.smoothScrollToPosition(testItems?.size ?: (0 - 1))
            }
        } else {
            Toast.makeText(context, "Message failed to send.", Toast.LENGTH_SHORT).show()
        }
    }
}