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
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.databinding.FragmentPointBinding
import com.fitmate.fitmate.domain.model.PointType
import com.fitmate.fitmate.presentation.ui.point.list.adapter.PointHistoryAdapter
import com.fitmate.fitmate.presentation.viewmodel.PointViewModel
import com.fitmate.fitmate.util.DateParseUtils
import com.fitmate.fitmate.util.customGetSerializable
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
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
    private var fitMateData: GetFitMateList? = null
    private var userImage: UserResponse? = null
    private val dealTextFieldList: Array<String> by lazy {
        arrayOf("전체", "입금", "출금")
    }
    private lateinit var selectedDateText:String
    private lateinit var selectedDealTypeText:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).goneNavigationBar()
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

        //포인트 기록 데이터 통신 감시
        observePointHistory()

        //어뎁터 초기화 및 설정
        adapterInit()

        //통신 시작(포인트 정보 및 포인트 기록 데이터)
        viewModel.getPointInfo(pointOwnerId, pointOwnerType.value)
        val (startDate, endDate)= getMonthStartAndEnd(dateTextFieldList[0])
        viewModel.getPagingPointHistory(pointOwnerId, pointOwnerType.value, startDate, endDate, 0, 10, null)



    }

    private fun adapterInit() {
        adapter = PointHistoryAdapter(fitMateData,userImage)
        binding.recyclerViewPointHistory.adapter = adapter

        //리사이클러뷰 어뎁터 업데이트 후 데이터가 있는지 확인하고 없으면 비어있다는 안내 메시지 띄우는작업
        adapter.addOnPagesUpdatedListener {
            val itemCount = adapter.itemCount
            if (itemCount == 0) {
                binding.recyclerViewPointHistory.visibility = View.GONE
                binding.textViewGuidePointHistoryEmpty.visibility = View.VISIBLE
            }else{
                binding.recyclerViewPointHistory.visibility = View.VISIBLE
                binding.textViewGuidePointHistoryEmpty.visibility = View.GONE
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
                    it.customGetSerializable<GetFitMateList>("fitMateData")?.let { fitMate->
                        fitMateData = fitMate
                    }
                    it.getString("createdAt")?.let { createString ->
                        createdAt = createString
                    }
                    it.getInt("groupId").let { groupId ->
                        pointOwnerId = groupId
                    }

                    dateTextFieldList = getYearMonthUntilToday(createdAt)
                }

                PointType.USER -> {
                    it.customGetSerializable<UserResponse>("myImage")?.let { myData->
                        userImage = myData
                    }
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
        // 한국 시간대
        val koreaZone = ZoneId.of("Asia/Seoul")

        // 현재 시간을 한국 시간으로 변환
        val nowKoreaTime = LocalDateTime.now(koreaZone)

        // 주어진 Instant 값을 한국 시간으로 변환
        val targetTime = LocalDateTime.ofInstant(DateParseUtils.stringToInstant(startDate), koreaZone)

        // DateTimeFormatter to format the dates
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM")

        // List to store the intervals
        val intervals = mutableListOf<String>()

        var current = if (targetTime.isAfter(nowKoreaTime)) targetTime else nowKoreaTime

        // Ensure to include the start month if it matches the current month
        while (current.isAfter(targetTime) || current.isEqual(targetTime)) {
            intervals.add(current.format(formatter))
            current = current.minusMonths(1)
        }
        intervals.add(targetTime.format(formatter))

        return intervals.distinct().sortedDescending().toTypedArray()
    }

    //날짜를 파라미터로 해당 월의 시작 시점과 끝 시점을 ISO 8601 문자열 형식으로 반환하는 메서드
    private fun getMonthStartAndEnd(yearMonth: String): Pair<String, String> {
        // DateTimeFormatter를 사용하여 "yyyy-MM" 형태의 문자열을 파싱
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
        val yearMonth = YearMonth.parse(yearMonth, formatter)

        // 해당 월의 첫째 날과 마지막 날의 LocalDateTime을 생성
        val startOfMonth = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC)
        val endOfMonth = yearMonth.atEndOfMonth().minusDays(1).atTime(23, 59, 59, 999_999_999).atOffset(ZoneOffset.UTC).toInstant()

        // Instant로 변환
        val startInstant = startOfMonth.toInstant()

        return Pair(DateParseUtils.instantToString(startInstant), DateParseUtils.instantToString(endOfMonth))
    }
}



