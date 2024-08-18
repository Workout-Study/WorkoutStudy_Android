package com.fitmate.fitmate.presentation.ui.chatting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitmate.fitmate.BuildConfig
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentChattingBinding
import com.fitmate.fitmate.domain.model.ChatItem
import com.fitmate.fitmate.domain.model.PointType
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.ChatAdapter
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.FitMate
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.FitMateListAdapter
import com.fitmate.fitmate.presentation.viewmodel.ChattingViewModel
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel
import com.fitmate.fitmate.util.DateParseUtils
import com.fitmate.fitmate.util.HeightProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class ChattingFragment : Fragment(R.layout.fragment_chatting) {

    companion object {
        const val chatServerAddress = BuildConfig.SERVER_ADDRESS
    }

    private lateinit var binding: FragmentChattingBinding
    private lateinit var heightProvider: HeightProvider

    @Inject
    lateinit var dbChatUseCase: DBChatUseCase
    private lateinit var chatAdapter: ChatAdapter
    private var isFirst = true
    private var userId: Int = -1
    private lateinit var groupCreatedAt: String
    private val viewModel: ChattingViewModel by viewModels()
    private val login: LoginViewModel by viewModels()
    private val group: GroupViewModel by viewModels()
    private var webSocket: WebSocket? = null
    private var penaltyAccountNumber: String? = null
    private var fitGroupId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //메인 액티비티의 바텀 네비 지우기
        (activity as MainActivity).goneNavigationBar()
        arguments?.getInt("fitGroupId")?.let { groupId ->
            fitGroupId = groupId
        }
        loadUserPreference() //유저 정보 가져오기
        group.getFitGroupDetail(fitGroupId) //서버로부터 그룹 정보 가저오기
        group.getFitMateList(fitGroupId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //가져온 그룹 정보 감시하기
        observeGroupDetail()
        observeFitMateList()                        // FitMate 리스트 가져오기
        initFragment(view)                          // 화면 바인딩
        initHeightProvider()                        // 메뉴 높이 조절
        setUpRecyclerView()                         // 채팅 아이템 리스트 설정
        observeChatResponse()                       // 새로 들어온 채팅 내역 동기화 후 채팅 보여주기
        //scrollBottom()                              // 들어 왔을 때 최하단 으로 이동
    }

    override fun onResume() {
        super.onResume()
        group.getFitMateList(fitGroupId)
    }

    private fun loadUserPreference() {
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
    }

    private fun initFragment(view: View) {
        binding = FragmentChattingBinding.bind(view)
        binding.fragment = this
        binding.containerExtraFunction.layoutTransition = null
        binding.toolbarFragmentChatting.setupWithNavController(findNavController())
    }


    //뒤로가기 메서드
    fun navigateToBack() {
        findNavController().popBackStack()
    }

    //드로워 레이아웃 여는 메서드
    fun toggleDrawer() {
        val fitMateListAdapter = FitMateListAdapter(emptyList(), "", "", login)
        binding.recyclerViewFragmentChattingForFitMateList.adapter = fitMateListAdapter
        binding.recyclerViewFragmentChattingForFitMateList.layoutManager =
            LinearLayoutManager(context)

        group.getMate.value?.let { fitMateList ->
            val leaderID = fitMateList.fitLeaderDetail.fitLeaderUserId
            val myID = userId.toString()
            binding.textViewFragmentChattingFitGroupSize.text =
                "대화 상대 " + fitMateList.fitMateDetails.size.toString()
            val filteredFitMates = fitMateList.fitMateDetails.map {
                FitMate(it.fitMateId, it.fitMateUserId, it.fitMateUserNickname, it.createdAt)
            }
            fitMateListAdapter.updateData(filteredFitMates, leaderID, myID)
        }

        binding.drawerLayoutForFragmentChatting.apply {
            if (isDrawerOpen(GravityCompat.END)) closeDrawer(GravityCompat.END) else openDrawer(
                GravityCompat.END
            )
        }
    }


    //하단 + 클릭 시 추가 기능 영역 여는 메서드
    fun toggleExtraFunctionContainer() {
        binding.containerExtraFunction.also { container ->
            container.visibility =
                if (container.visibility == View.GONE) View.VISIBLE else View.GONE
            if (container.visibility == View.VISIBLE) hideKeyboard()
        }
    }

    //피트오프 신청화면으로 이동하는 메서드
    fun navigateToRegisterFitOff() {
        findNavController().navigate(R.id.makeFitOffFragment)
    }

    //그룹 피트오프 현황으로 이동하는 메서드
    fun navigateToViewFitOffFragment() {
        group.getMate.value?.let { fitMate ->
            val bundle = Bundle().apply {
                putSerializable("fitOffOwnerNameInfo", fitMate)
            }
            findNavController().navigate(R.id.viewFitOffFragment, bundle)
        }
    }

    //투표 화면으로 이동하는 메서드
    fun navigateToVoteFragment() {
        val bundle = Bundle().apply {
            putInt("groupId", fitGroupId)
        }
        findNavController().navigate(R.id.groupVoteFragment, bundle)
    }

    //포인트 화면으로 이동하는 메서드
    fun navigateToPointFragment() {
        if (::groupCreatedAt.isInitialized) {
            val bundle = Bundle().apply {
                putSerializable("pointOwnerType", PointType.GROUP)
                putString("createdAt", groupCreatedAt)
                putInt("groupId", fitGroupId)
            }
            findNavController().navigate(R.id.pointFragment, bundle)
        } else {
            Toast.makeText(requireContext(), "알 수 없는 오류 발생! 잠시후 이용해주세요!", Toast.LENGTH_SHORT).show()
        }
    }

    //운동 현황 화면으로 이동하는 메서드
    fun navigateToProgressFragment() {
        if (fitGroupId != -1) {
            val bundle = Bundle().apply {
                putInt("groupId", fitGroupId)
            }
            findNavController().navigate(R.id.groupProgressFragment, bundle)
        } else {
            Toast.makeText(requireContext(), "알 수 없는 오류 발생! 잠시후 이용해주세요!", Toast.LENGTH_SHORT).show()
        }
    }


    //키보드 닫는 메서드
    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    //키보드 높이 계산 메서드
    private fun initHeightProvider() {
        (activity as MainActivity).let {
            heightProvider = HeightProvider(it).init().setHeightListener { height ->
                if (height > 0) {
                    binding.containerExtraFunction.visibility = View.GONE
                    binding.containerExtraFunction.layoutParams.height = height
                    binding.recyclerViewFragmentChatting.apply {
                        post {
                            if ((adapter?.itemCount
                                    ?: 0) > 0
                            ) smoothScrollToPosition(adapter!!.itemCount - 1)
                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        chatAdapter = ChatAdapter()
        val manager = LinearLayoutManager(requireContext())
        //manager.stackFromEnd = true
        //manager.reverseLayout = true
        with(binding.recyclerViewFragmentChatting) {
            chatAdapter.apply { setCurrentUserFitMateId(userId) }
            adapter = chatAdapter
            layoutManager = manager
            itemAnimator = null
        }
    }

    private fun setupWebSocketConnection() {
        // LoggingInterceptor 설정
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // OkHttpClient 설정
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val request = Request.Builder()
            .url("ws://${chatServerAddress}:8888/chat?fitGroupId=${fitGroupId}&userId=${userId}")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("tlqkf", "소켓 연결 Success")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {

                val messageObject = JSONObject(text)
                val receivedFitGroupId = messageObject.getInt("fitGroupId")
                if (receivedFitGroupId == this@ChattingFragment.fitGroupId) {
                    val messageId = messageObject.getString("messageId")
                    val userId = messageObject.getInt("userId")
                    val message = messageObject.getString("message")
                    val messageTime = messageObject.getString("messageTime")
                    val messageType = messageObject.getString("messageType")
                    val resultTime =  messageTime.replace("T"," ")
                    val parsedMessageTime = DateParseUtils.stringToInstant(resultTime)

                    val chatItem = ChatItem(
                        messageId,
                        receivedFitGroupId,
                        userId,
                        message,
                        parsedMessageTime,
                        messageType
                    )
                    Log.d("tlqkf", "소켓에서 날아온 데이터 : " + chatItem.toString())

                    (activity as MainActivity).runOnUiThread {
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                dbChatUseCase.insert(chatItem)
                            }
                            val currentList = chatAdapter.currentList.toMutableList()
                            currentList.add(chatItem)
                            chatAdapter.submitList(currentList.toMutableList()) {
                                chatAdapter.notifyItemInserted(chatAdapter.currentList.lastIndex)
                                binding.recyclerViewFragmentChatting.scrollToPosition(chatAdapter.currentList.lastIndex)
                            }
                        }
                    }
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                isFirst = !isFirst
                Log.d("tlqkf", "소켓 onClosed")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                isFirst = !isFirst
                Log.d("tlqkf", "onFailure 오류 원인:$t")
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

            chatAdapter.submitList(chatItems) {
                //TODO 여기 한번 확인 필요
                chatAdapter.notifyItemInserted(chatAdapter.currentList.lastIndex)
                //binding.recyclerViewFragmentChatting.scrollToPosition(chatAdapter.currentList.lastIndex)
                if (isFirst) {
                    setupWebSocketConnection()
                }
                isFirst = !isFirst
            }
        }
    }

    private fun observeChatResponse() {
        viewModel.retrieveMessage()
        lifecycleScope.launch {
            viewModel.chatResponse.collect { chatResponse ->
                if (chatResponse != null) {
                    Log.d("tlqkf", "리트라이브 옵저버 시작됨")
                    val newItems =
                        chatResponse.messages.filter { it.fitGroupId == fitGroupId }
                            .map { message ->
                                ChatItem(
                                    messageId = message.messageId,
                                    fitGroupId = message.fitGroupId,
                                    userId = message.userId,
                                    message = message.message,
                                    messageTime = DateParseUtils.stringToInstant(message.messageTime),
                                    messageType = message.messageType
                                )
                            }/*.reversed()*/

                    newItems.forEach { newItem -> dbChatUseCase.insert(newItem) }
                    loadChatMessage()
                } else {
                    Log.d("tlqkf", "리트라이브 옵저버 시작 안된상태로 소벳 열기")
                    loadChatMessage()
                }
            }
        }
    }

    private fun observeGroupDetail() {
        group.groupDetail.observe(viewLifecycleOwner) {
            groupCreatedAt = it.createdAt
        }
    }

    private fun observeFitMateList() {
        group.getMate.observe(viewLifecycleOwner) { fitMateList ->
            val leaderId = fitMateList.fitLeaderDetail.fitLeaderUserId
            val myID = userId.toString()
            val filteredFitMates = fitMateList.fitMateDetails.map {
                FitMate(it.fitMateId, it.fitMateUserId, it.fitMateUserNickname, it.createdAt)
            }
            (binding.recyclerViewFragmentChattingForFitMateList.adapter as FitMateListAdapter)
                .updateData(filteredFitMates, leaderId, myID)
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

    fun chatSend() {
        val message = binding.editTextChattingMySpeech.text
        if (message.isNotEmpty()) {
            sendChatMessage(message.toString())
        }
    }

    private fun sendChatMessage(message: String) {
        val messageId = UUID.randomUUID().toString()
        val timeNow = Instant.now()
        val isMessageSent = sendMessage(messageId, message, DateParseUtils.instantToString(timeNow))

        if (isMessageSent) {
            binding.editTextChattingMySpeech.setText("")
            val newChatting = ChatItem(
                messageId = messageId,
                fitGroupId = fitGroupId,
                userId = userId,
                message = message,
                messageTime = timeNow,
                messageType = "CHATTING"
            )
            lifecycleScope.launch { dbChatUseCase.insert(newChatting) }
            val chatItems = chatAdapter.currentList.toMutableList()
            chatItems.add(newChatting)
            chatAdapter.submitList(chatItems) {
                chatAdapter.notifyItemInserted(chatAdapter.currentList.lastIndex)
                binding.recyclerViewFragmentChatting.scrollToPosition(chatItems.lastIndex)
            }
        } else {
            Toast.makeText(context, "채팅 전송 실패! 잠시 후 시도해 주새요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessage(messageId: String, message: String, timeNow: String): Boolean {
        Log.d("tlqkf",timeNow)
        val jsonObject = JSONObject().apply {
            put("messageId", messageId)
            put("fitGroupId", fitGroupId)
            put("userId", userId)
            put("message", message)
            put("messageTime", timeNow)
            put("messageType", "CHATTING")
        }
        Log.d("tlqkf", "전송된 채팅 데이터 : $jsonObject")
        return webSocket?.send(jsonObject.toString()) ?: false
    }

    override fun onPause() {
        super.onPause()
        webSocket?.close(1000, "Fragment Paused")
        if (isFirst) isFirst = false
    }
}