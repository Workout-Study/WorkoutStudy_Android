package com.fitmate.fitmate.presentation.ui.point

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentPointBinding
import com.fitmate.fitmate.domain.model.PointType
import com.fitmate.fitmate.presentation.ui.point.list.adapter.PointHistoryAdapter
import com.fitmate.fitmate.presentation.viewmodel.PointViewModel
import com.fitmate.fitmate.util.customGetSerializable
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAdjusters

@AndroidEntryPoint
class PointFragment : Fragment(R.layout.fragment_point) {
    private lateinit var binding: FragmentPointBinding
    private val viewModel: PointViewModel by viewModels()
    private lateinit var adapter: PointHistoryAdapter
    private lateinit var pointOwnerType: PointType
    private var pointOwnerId: Int = -1
    private lateinit var createdAt: String
    private lateinit var dateTextFieldList: Array<String>
    private val dealTextFieldList: Array<String> by lazy {
        arrayOf("전체", "입금", "출금")
    }
    private lateinit var selectedDateText:String
    private lateinit var selectedDealTypeText:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //번들 값에 따라 pointOwnerId, pointOwnerType, createAt값 설정하는 메서드
        getArgumentAndSettingCreateAt()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPointBinding.bind(view)
        //binding 초기 설정
        binding.apply {
            viewModel = viewModel

            toolbar.setupWithNavController(findNavController())

            if (pointOwnerType == PointType.GROUP) {
                binding.toolbar.title = getString(R.string.group_point_title)
            }
        }

        //어뎁터 초기화 및 설정
        adapterInit()

        //통신 시작(포인트 정보 및 포인트 기록 데이터)
        viewModel.getPointInfo(pointOwnerId, pointOwnerType.value)
        viewModel.getPagingPointHistory(pointOwnerId, pointOwnerType.value, null, null, 0, 10, null)

        //포인트 기록 데이터 통신 감시
        observePointHistory()

    }

    private fun adapterInit() {
        adapter = PointHistoryAdapter()
        binding.recyclerViewPointHistory.adapter = adapter

        //리사이클러뷰 어뎁터 업데이트 후 데이터가 있는지 확인하고 없으면 비어있다는 안내 메시지 띄우는작업
        adapter.addOnPagesUpdatedListener {
            val test = adapter.itemCount
            if (test == 0) {
                binding.recyclerViewPointHistory.visibility = View.GONE
                binding.textViewGuidePointHistoryEmpty.visibility = View.VISIBLE
            }
        }

        //날짜 선택 필드 설정
        if (::dateTextFieldList.isInitialized && dateTextFieldList.isNotEmpty()) {
            (binding.textDrawerTargetCalendar as? MaterialAutoCompleteTextView)?.apply {
                setDropDownBackgroundTint(Color.WHITE)
                setSimpleItems(dateTextFieldList)
                setText(dateTextFieldList[0], false) // 초기 텍스트 선택 설정
                selectedDateText = dateTextFieldList[0] //선택된 날짜 초기화

                //날짜 선택 리스너 설정
                onItemClickListener =
                    AdapterView.OnItemClickListener { adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
                        if (selectedDateText != dateTextFieldList[i]) {
                            Log.d("tlqkf","날짜 변경")
                            val tradeType: String? = when(selectedDealTypeText){
                                "전체" -> {null}
                                "입금" -> {"DEPOSIT"}
                                "출금" -> {"WITHDRAW"}
                                else -> {null}
                            }
                            val (startDate, endDate) = getMonthStartAndEnd(dateTextFieldList[i])
                            viewModel.getPagingPointHistory(pointOwnerId, pointOwnerType.value, startDate, endDate, 0, 10, tradeType)
                            selectedDateText = dateTextFieldList[i]
                        }
                    }
            }
        }

        //입금 출금 필드 설정
        if (dealTextFieldList.isNotEmpty()) {
            (binding.textDrawerTargetDealType as? MaterialAutoCompleteTextView)?.apply {
                setDropDownBackgroundTint(Color.WHITE)
                setSimpleItems(dealTextFieldList)
                setText(dealTextFieldList[0], false) //초기 입금 출금 선택 설정
                selectedDealTypeText = dealTextFieldList[0] //선택된 입금 출금 초기화(전체로 설정)

                //입금 출금 선택 리스너 설정
                onItemClickListener =
                    AdapterView.OnItemClickListener { adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
                        if (selectedDealTypeText != dealTextFieldList[i]) {
                            Log.d("tlqkf","입출금 변경")
                            val (startDate, endDate) = getMonthStartAndEnd(selectedDateText)
                            val tradeType: String? = when(dealTextFieldList[i]){
                                "전체" -> {null}
                                "입금" -> {"DEPOSIT"}
                                "출금" -> {"WITHDRAW"}
                                else -> {null}
                            }
                            viewModel.getPagingPointHistory(pointOwnerId, pointOwnerType.value, startDate, endDate, 0, 10, tradeType)
                            selectedDealTypeText = dealTextFieldList[i]
                        }
                    }
            }

        }
    }

    private fun getArgumentAndSettingCreateAt() {
        arguments?.let {
            it.customGetSerializable<PointType>("pointOwnerType")?.let {
                pointOwnerType = it
            }
            when (pointOwnerType) {
                PointType.GROUP -> {
                    it.getString("createdAt")?.let { createString ->
                        createdAt = createString
                    }
                    it.getInt("groupId").let { groupId ->
                        pointOwnerId = groupId
                    }

                    dateTextFieldList = getYearMonthUntilToday(createdAt.dropLast(1))
                }

                PointType.USER -> {
                    val userPreference = (activity as MainActivity).loadUserPreference()
                    pointOwnerId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
                    createdAt = userPreference[4].toString()

                    dateTextFieldList = getYearMonthUntilToday(createdAt)
                }
            }
        }
    }

    private fun observePointHistory() {
        viewModel.run {
            viewModelScope.launch {
                pagingData.collectLatest { pagingData ->
                    if (pagingData != null) {
                        binding.recyclerViewPointHistory.visibility = View.VISIBLE
                        binding.textViewGuidePointHistoryEmpty.visibility = View.GONE
                        adapter.submitData(lifecycle,pagingData)
                    }
                }
            }
            pointInfo.observe(viewLifecycleOwner) {
                binding.textViewPointBalance.text = getString(R.string.point_amount, it.balance)
            }
        }
    }

    //createdAt을 사용해서 오늘 날짜로부터 모든 월을 반환하는 메서드
    private fun getYearMonthUntilToday(startDate: String): Array<String> {
        // 날짜 포맷 설정
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")

        // 시작 날짜를 LocalDateTime으로 변환 (UTC 기준)
        val startLocalDateTime = LocalDateTime.parse(startDate, formatter)

        // UTC 날짜를 한국 날짜로 변환
        val startKoreaDate =
            startLocalDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDate()

        // 현재 한국 날짜 (KST) 가져오기
        val today = LocalDate.now(ZoneId.of("Asia/Seoul"))

        // 날짜 리스트 초기화
        val dateSet = LinkedHashSet<String>()

        // 현재 날짜부터 시작 날짜까지 루프 (역순)
        var currentDate = today
        val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
        while (!currentDate.isBefore(startKoreaDate)) {
            dateSet.add(currentDate.format(yearMonthFormatter))
            currentDate = currentDate.minusMonths(1)
        }

        // 리스트를 배열로 변환하여 반환
        return dateSet.toTypedArray()
    }

    //날짜를 파라미터로 해당 월의 시작 시점과 끝 시점을 ISO 8601 문자열 형식으로 반환하는 메서드
    private fun getMonthStartAndEnd(yearMonth: String): Pair<String, String> {
        // 날짜 포맷 설정
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM")

        try {
            // 입력 받은 년-월을 LocalDate로 변환 (첫 번째 날)
            val firstDayOfMonth = LocalDate.parse("$yearMonth-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            // 해당 월의 마지막 날 계산
            val lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth())

            // ISO 8601 형식으로 변환 (UTC 시간대)
            val startDateTime = LocalDateTime.of(firstDayOfMonth, LocalDateTime.MIN.toLocalTime()).atOffset(ZoneOffset.UTC)
            val endDateTime = LocalDateTime.of(lastDayOfMonth, LocalDateTime.MAX.toLocalTime()).atOffset(ZoneOffset.UTC)

            // 결과를 문자열로 반환
            val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            return Pair(startDateTime.format(isoFormatter), endDateTime.format(isoFormatter))
        } catch (e: DateTimeParseException) {
            Log.d("tlqkf","날짜 파싱 실패했어")
            throw IllegalArgumentException("Invalid date format: $yearMonth. Expected format is yyyy-MM.")
        }
    }
}



