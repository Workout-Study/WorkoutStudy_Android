package com.fitmate.fitmate.presentation.ui.myfit

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.CalendarDayLayoutBinding
import com.fitmate.fitmate.databinding.FragmentMyfitBinding
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail
import com.fitmate.fitmate.presentation.ui.myfit.list.adapter.DayListAdapter
import com.fitmate.fitmate.presentation.ui.myfit.list.adapter.MonthListAdapter
import com.fitmate.fitmate.presentation.viewmodel.MyFitViewModel
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitGroupProgressAdapter
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitHistoryAdapter
import com.fitmate.fitmate.util.ControlActivityInterface
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Duration.Companion.days

@AndroidEntryPoint
class MyFitFragment : Fragment() {
    private lateinit var binding: FragmentMyfitBinding
    private lateinit var controlActivityInterface: ControlActivityInterface

    private val fitGroupProgressAdapter: MyFitGroupProgressAdapter by lazy {
        MyFitGroupProgressAdapter(
            requireContext()
        )
    }

    private lateinit var calendarAdapter: MonthListAdapter
    private lateinit var fitHistoryAdapter: MyFitHistoryAdapter
    private var selectedDate: LocalDate? = null

    private val viewModel: MyFitViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyfitBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.myFitFragment = this
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.viewNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //툴바 버튼 클릭 리스너 초기화
        initButtonClickListener()

        //그룹 fitProgress 리사이클러뷰 데이터 연결
        observeFitProgress()

        //운동 기록 리사이클러뷰 mock 데이터 연결
        initMyFitHistoryRecyclerView()

        // 캘린더 연결
        initCalendar()

        //캘린더에서 통신한 운동 기록 데이터를 감시
        observeMyfitHistoryInCalendar()

        //플로팅 버튼
        setToggleAppBar()
    }

    fun onClickGuideEnterGroup() {
        val bundle = Bundle()
        bundle.putInt("viewPagerPosition", 1)
        findNavController().navigate(R.id.action_myFitFragment_to_homeFragment, bundle)
    }

    private fun observeMyfitHistoryInCalendar() {
        viewModel.myFitRecordHistory.observe(viewLifecycleOwner) {
            calendarAdapter.calendarHandler.fitRecordHistoryDataResult = it
            calendarAdapter.notifyDataSetChanged()
        }
    }

    private fun initCalendar() {

        //캘린더 그리는 작업 수행
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                container.textView.text = data.date.dayOfMonth.toString()
                val currentDate = LocalDate.now()  // 현재 날짜 가져오기
                if (data.position == DayPosition.MonthDate) {
                    container.textView.visibility = View.VISIBLE
                    if (container.day.date == currentDate) {
                        container.textView.setTextColor(Color.RED)  // 원하는 색상으로 변경
                        container.textView.setBackgroundResource(R.drawable.bg_border_selected)  // 원하는 배경 설정
                    }else{
                        container.textView.setTextColor(Color.BLACK)
                    }

                    if (container.day.date == selectedDate) {
                        // If this is the selected date, show a round background and change the text color.
                        container.textView.setTextColor(Color.WHITE)
                        container.textView.setBackgroundResource(R.drawable.linear_button_gradient)
                    } else {
                        // If this is NOT the selected date, remove the background and reset the text color.
                        if (container.day.date == currentDate) {
                            container.textView.setTextColor(Color.RED)  // 원하는 색상으로 변경
                            container.textView.setBackgroundResource(R.drawable.bg_border_selected)  // 원하는 배경 설정
                        }else{
                            container.textView.setTextColor(Color.BLACK)
                            container.textView.background = null
                        }

                    }

                } else {
                    container.textView.setTextColor(Color.WHITE)
                    container.textView.visibility = View.INVISIBLE
                }

            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val firstDayOfWeek = firstDayOfWeekFromLocale()


        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)//캘린더 세부 설정
        binding.calendarView.scrollToMonth(currentMonth)//캘린더 오늘 날짜로 스크롤 수행
        binding.calendarView.monthScrollListener = { updateTitle() }//캘린더 스크롤시 수행할 작업 설정
        val daysOfWeek = daysOfWeek()

        //캘린더 상단 년도와 월 표시하는 작업 설정
        binding.calendarView.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                if (container.titlesContainer.tag != null) {
                    container.titlesContainer.tag = data.yearMonth
                    container.titlesContainer.children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek[index]
                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
                            textView.text = title
                        }
                }
            }
        }
    }

    private fun initMyFitHistoryRecyclerView() {
        fitHistoryAdapter = MyFitHistoryAdapter(requireContext())
        binding.recyclerViewMyFitFragmentFitHistory.apply {
            fitHistoryAdapter = MyFitHistoryAdapter(requireContext())
            adapter = fitHistoryAdapter
        }
    }

    private fun initButtonClickListener() {

        binding.buttonFragmentMyFitFitOff.setOnClickListener {
            //findNavController().navigate(R.id.action_myFitFragment_to_myFitOffFragment)
            Toast.makeText(requireContext(), "추후 업데이트 예정입니다", Toast.LENGTH_SHORT).show()
        }
        binding.buttonFragmentMyFitRecord.setOnClickListener {
            findNavController().navigate(R.id.action_myFitFragment_to_certificateFragment)
        }
    }

    private fun observeFitProgress() {
        observeNetworkMyProgress()
        viewModel.getMyFitProgress("567843")
    }

    private fun observeNetworkMyProgress() =
        viewModel.fitProgressItem.observe(viewLifecycleOwner) { fitProgressList ->
            fitGroupProgressAdapter.submitList(fitProgressList) {
                binding.recyclerviewMyFitFragmentMyFitProgress.adapter = fitGroupProgressAdapter
            }
        }

    private fun setToggleAppBar() {
        binding.floatingButtonScrollTop.setOnClickListener {
            binding.contentsNestedSScrollView.fullScroll(ScrollView.FOCUS_UP)
            binding.toolbarLayout.setExpanded(true, true)
        }
    }

    fun Month.displayText(short: Boolean = true): String {
        val style = if (short) TextStyle.SHORT else TextStyle.FULL
        return getDisplayName(style, Locale.KOREAN)
    }

    private fun updateTitle() {
        val month = binding.calendarView.findFirstVisibleMonth()?.yearMonth ?: return
        binding.exOneYearText.text = month.year.toString()
        binding.exOneMonthText.text = month.month.displayText(short = false)
    }

    inner class CalendarHandler() {
        var innerAdapter: DayListAdapter? = null
        val vM = viewModel
        var fitRecordHistoryDataResult: List<MyFitRecordHistoryDetail> = emptyList()
        var myFitHistoryAdapter: MyFitHistoryAdapter? = null
        var tempMonth: Int? = null
        fun networkMyFitRecordHistory(year: Int, month: Int) {
            if (tempMonth != month) {
                tempMonth = month
                val (startDate, endDate) = getStartAndEndInstantsForYearMonth(year, month)
                viewModel.getMyFitRecordHistory("567843", startDate, endDate)
            }
        }

        fun getStartAndEndInstantsForYearMonth(year: Int, month: Int): Pair<String, String> {
            // 입력된 년도와 월로 LocalDate 객체 생성
            val firstDayOfMonth = LocalDate.of(year, month, 1)
            val lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1)

            // 해당 월의 첫 번째 날의 00:00:00과 마지막 날의 23:59:59의 Instant 구하기
            val startInstant =
                firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toString()
            val endInstant =
                lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1)
                    .toInstant().toString()

            return Pair(startInstant, endInstant)
        }
    }

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        // With ViewBinding
        val textView = CalendarDayLayoutBinding.bind(view).calendarDayText

        lateinit var day: CalendarDay

        init {
            view.setOnClickListener {
                // Check the day position as we do not want to select in or out dates.
                if (day.position == DayPosition.MonthDate) {
                    // Keep a reference to any previous selection
                    // in case we overwrite it and need to reload it.
                    val currentSelection = selectedDate
                    if (currentSelection == day.date) {
                        // If the user clicks the same date, clear selection.
                        selectedDate = null
                        // Reload this date so the dayBinder is called
                        // and we can REMOVE the selection background.
                        binding.calendarView.notifyDateChanged(currentSelection)
                    } else {
                        selectedDate = day.date
                        // Reload the newly selected date so the dayBinder is
                        // called and we can ADD the selection background.
                        binding.calendarView.notifyDateChanged(day.date)
                        if (currentSelection != null) {
                            // We need to also reload the previously selected
                            // date so we can REMOVE the selection background.
                            binding.calendarView.notifyDateChanged(currentSelection)
                        }
                    }
                }
            }
        }
    }

    inner class MonthViewContainer(view: View) : ViewContainer(view) {
        // Alternatively, you can add an ID to the container layout and use findViewById()
        val titlesContainer = view as ViewGroup
    }
}