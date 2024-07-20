package com.fitmate.fitmate.presentation.ui.chatting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.ChatActivity
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentChattingBinding
import com.fitmate.fitmate.domain.model.ChatItem
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.ChatAdapter
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.FitMate
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.FitMateListAdapter
import com.fitmate.fitmate.presentation.viewmodel.ChattingViewModel
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
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

    companion object { const val chatServerAddress = BuildConfig.SERVER_ADDRESS }
    private lateinit var binding: FragmentChattingBinding
    private lateinit var heightProvider: HeightProvider
    @Inject lateinit var dbChatUseCase: DBChatUseCase
    private var userId: Int = -1
    private val TAG = "ChattingFragment"
    private val viewModel: ChattingViewModel by viewModels()
    private val group: GroupViewModel by viewModels()
    private var webSocket: WebSocket? = null
    private var penaltyAccountNumber: String? = null
    private var fitGroupId: Int = -1
    private lateinit var createdAt: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fitGroupId = requireArguments().getInt("fitGroupId", -1)
        userId = requireArguments().getInt("userId", -1)
        Log.d(TAG, "fitGroupId = $fitGroupId, userId = $userId")
        group.getFitGroupDetail(fitGroupId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment(view)                          // 화면 바인딩
        loadUserPreference()
        activeBackButton()                          // Back 버튼 커스텀
        setupClickListeners()                       // + 메뉴 클릭 리스너
        initHeightProvider()                        // 메뉴 높이 조절
        setUpRecyclerView()                         // 채팅 아이템 리스트 설정
        setupWebSocketConnection("$fitGroupId")     // 해당 피트 그룹 웹소캣 연결(계속 들어오는 데이터 room에 저장하고 불러오기)
        loadChatMessage()                           // 채팅 아이템 실시간 load(room에 있는 데이터 가져오기)
        observeChatResponse()                       // 새로 들어온 채팅 내역 load & save(최초 한번 수행 됨)
        scrollBottom()                              // 들어 왔을 때 최하단 으로 이동
        chatSend()                                  // 채팅 전송

    }

    private fun loadUserPreference() {
        val userPreference = (activity as ChatActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
    }

    private fun initFragment(view: View) {
        binding = FragmentChattingBinding.bind(view)
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.containerExtraFunction.layoutTransition = null
        binding.toolbarFragmentChatting.setupWithNavController(findNavController())
    }

    private fun activeBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }

    private fun setupClickListeners() {
        binding.run {
            val clickMappings = mapOf(
                containerFitOffSituation.chattingExtraFunctionButton to { navigate(R.id.groupFitOffFragment, false) },
                containerFitOffApply.chattingExtraFunctionButton to { navigate(R.id.groupFitOffFragment, false) },
                containerFitVote.chattingExtraFunctionButton to { navigate(R.id.groupVoteFragment, true) },
                containerFitSituation.chattingExtraFunctionButton to { navigate(R.id.groupProgressFragment, true) },
                containerGroupPointSituation.chattingExtraFunctionButton to { navigate(R.id.groupFitOffFragment, false) },
                imageViewChattingFragmentOpenContentList to { toggleExtraFunctionContainer() },
                buttonFragmentChattingExit to { activity?.finish() },
                imageViewChattingToolbarForDrawerLayout to { toggleDrawer() },
            )
            clickMappings.forEach { (button, action) -> button.setOnClickListener { action() } }
        }
    }

    //TODO 포인트 화면 클릭시 해당 메서드 호출해야함.
    private fun navigateWithCreatedAt(fragmentId: Int) {
        group.groupDetail.observe(viewLifecycleOwner) {groupDetail ->
            createdAt = groupDetail.createdAt
        }
        val bundle = Bundle()
        bundle.putString("createdAt",createdAt)
        findNavController().navigate(fragmentId, bundle)
    }

    private fun navigate(fragmentId: Int, isBundle: Boolean) {
        if(isBundle){
            val bundle = Bundle()
            bundle.putInt("groupId", fitGroupId)
            findNavController().navigate(fragmentId, bundle)
        } else {
            Toast.makeText(context, "추후 업데이트 예정입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigate() {
        val intent = Intent(activity, MainActivity::class.java).apply { putExtra("navigateTo", "certificateFragment") }
        startActivity(intent)
        activity?.finish()
    }

    private fun copyAccountNum() {
        hideKeyboard()
        group.groupDetail.observe(viewLifecycleOwner) {groupDetail ->
            penaltyAccountNumber = groupDetail.penaltyAccountNumber
        }
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Group Account Number", penaltyAccountNumber)
        clipboard.setPrimaryClip(clip)
    }

    private fun toggleDrawer() {
        group.getFitMateList(fitGroupId)
        val includedLayout = view?.findViewById<View>(R.id.includeFragmentChattingMyInfo)
        val textView = includedLayout?.findViewById<TextView>(R.id.textViewItemChattingFitMateName)
        val fitMateListAdapter = FitMateListAdapter(emptyList())
        binding.recyclerViewFragmentChattingForFitMateList.adapter = fitMateListAdapter
        binding.recyclerViewFragmentChattingForFitMateList.layoutManager = LinearLayoutManager(context)

        group.getMate.observe(viewLifecycleOwner) { fitMateList ->
            val isLeader = fitMateList.fitLeaderDetail.fitLeaderUserId == userId.toString()
            Log.d("woojugoing_isLeader", isLeader.toString())
            fitMateList.fitMateDetails.firstOrNull { it.fitMateId == userId }?.let { textView?.text = it.fitMateUserId }
            val filteredFitMates = fitMateList.fitMateDetails.filter { it.fitMateId != userId }.map { FitMate(it.fitMateId, it.fitMateUserId,it.createdAt) }
            (binding.recyclerViewFragmentChattingForFitMateList.adapter as FitMateListAdapter).updateData(filteredFitMates, isLeader)
        }

        binding.drawerLayoutForFragmentChatting.apply { if (isDrawerOpen(GravityCompat.END)) closeDrawer(GravityCompat.END) else openDrawer(GravityCompat.END) }
    }


    private fun toggleExtraFunctionContainer() {
        binding.containerExtraFunction.also { container ->
            container.visibility = if (container.visibility == View.GONE) View.VISIBLE else View.GONE
            if (container.visibility == View.VISIBLE) hideKeyboard()
        }
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
                    binding.recyclerViewFragmentChatting.apply {
                        post { if ((adapter?.itemCount ?: 0) > 0) smoothScrollToPosition(adapter!!.itemCount - 1) }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        with(binding.recyclerViewFragmentChatting) {
            adapter = ChatAdapter().apply { setCurrentUserFitMateId(userId) }
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
        }
    }

    private fun setupWebSocketConnection(fitGroupId: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://${chatServerAddress}:8888/chat?fitGroupId=${fitGroupId}&userId=${userId}").build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WebSocket", "Success")
            }
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Receiving : $text")
                val messageObject = JSONObject(text)
                val receivedFitGroupId = messageObject.getInt("fitGroupId")
                if (receivedFitGroupId == this@ChattingFragment.userId) {
                    val messageId = messageObject.getString("messageId")
                    val userId = messageObject.getInt("userd")
                    val message = messageObject.getString("message")
                    val messageTime = messageObject.getString("messageTime")
                    val messageType = messageObject.getString("messageType")

                    val parsedMessageTime = LocalDateTime.parse(messageTime, DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                        .appendPattern("[.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]")
                        .appendPattern("'Z'")
                        .toFormatter()
                        .withZone(ZoneId.systemDefault()))

                    val chatItem = ChatItem(messageId, receivedFitGroupId, userId, message, parsedMessageTime, messageType)

                    activity?.runOnUiThread {
                        lifecycleScope.launch {
                            dbChatUseCase.insert(chatItem)
                            val adapter = binding.recyclerViewFragmentChatting.adapter as? ChatAdapter
                            val currentList = adapter?.currentList?.toMutableList() ?: mutableListOf()
                            currentList.add(chatItem)
                            adapter?.submitList(currentList)
                            binding.recyclerViewFragmentChatting.smoothScrollToPosition(adapter?.itemCount ?: (0 - 1))
                        }
                    }
                }
            }
        })
    }

    private fun loadChatMessage() {
        lifecycleScope.launch {
            val chatItems = dbChatUseCase.getChatItemsByFitGroupId(fitGroupId).map { chatEntity ->
                ChatItem(
                    messageId = chatEntity.messageId,
                    fitGroupId = chatEntity.fitGroupId,
                    userId = chatEntity.userId,
                    message = chatEntity.message,
                    messageTime = chatEntity.messageTime,
                    messageType = chatEntity.messageType
                )
            }

            val adapter = binding.recyclerViewFragmentChatting.adapter as? ChatAdapter ?: ChatAdapter().also { binding.recyclerViewFragmentChatting.adapter = it }
            adapter.setCurrentUserFitMateId(userId)
            adapter.submitList(chatItems) {
                binding.recyclerViewFragmentChatting.post {
                    binding.recyclerViewFragmentChatting.scrollToPosition(
                        adapter.itemCount - 1
                    )
                }
            }
        }
    }

    private fun observeChatResponse() {
        viewModel.retrieveMessage()
        lifecycleScope.launch {
            viewModel.chatResponse.collect { chatResponse ->
                if (chatResponse?.messages != null && chatResponse.messages.isNotEmpty()) {
                    val newItems = chatResponse.messages.filter { it.fitGroupId == fitGroupId }.map { message ->
                        ChatItem(
                            messageId = message.messageId,
                            fitGroupId = message.fitGroupId,
                            userId = message.userId,
                            message = message.message,
                            messageTime = LocalDateTime.parse(
                                message.messageTime, DateTimeFormatterBuilder()
                                .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                                .appendPattern("[.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]")
                                .appendPattern("'Z'")
                                .toFormatter()),
                            messageType = message.messageType )}.reversed()

                    newItems.forEach { newItem -> dbChatUseCase.insert(newItem) }
                    loadChatMessage()
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

    private fun chatSend() {
        binding.ImageViewChattingSendMySpeech.setOnClickListener {
            val message = binding.editTextChattingMySpeech.text.toString()
            if (message.isNotEmpty()) sendChatMessage(message)
        }
    }

    private fun sendChatMessage(message: String) {
        val messageId = UUID.randomUUID().toString()
        val timeNow = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val isMessageSent = sendMessage(messageId, message, timeNow)
        Log.d("send_message", timeNow.toString())

        if (isMessageSent) {
            val newChatItem = ChatItem(messageId, fitGroupId, userId, message, timeNow, "CHATTING")
            Log.d("sendChatMessage", userId.toString())
            lifecycleScope.launch { dbChatUseCase.insert(newChatItem) }

            binding.editTextChattingMySpeech.setText("")
            val testItems = (binding.recyclerViewFragmentChatting.adapter as? ChatAdapter)?.currentList?.toMutableList()
            binding.recyclerViewFragmentChatting.post {
                binding.recyclerViewFragmentChatting.smoothScrollToPosition(testItems?.size ?: (0 - 1))
            }
        } else {
            Toast.makeText(context, "Message failed to send.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessage(messageId: String, message: String, timeNow: LocalDateTime): Boolean {
        val jsonObject = JSONObject().apply {
            put("messageId", messageId)
            put("fitGroupId", fitGroupId)
            put("userId", userId)
            put("message", message)
            put("messageTime", timeNow)
            put("messageType", "CHATTING")
        }
        return webSocket?.send(jsonObject.toString()) ?: false
    }

    override fun onPause() {
        super.onPause()
        webSocket?.close(1000, "Fragment Paused")
    }
}