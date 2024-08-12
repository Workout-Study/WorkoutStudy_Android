package com.fitmate.fitmate.presentation.ui.myfit

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.CalendarDayLayoutBinding
import com.fitmate.fitmate.databinding.FragmentMyfitBinding
import com.fitmate.fitmate.presentation.viewmodel.MyFitViewModel
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitHistoryAdapter
import com.fitmate.fitmate.util.ControlActivityInterface
import com.fitmate.fitmate.util.DateParseUtils
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@AndroidEntryPoint
class MyFitFragment : Fragment() {
    private lateinit var binding: FragmentMyfitBinding
    private lateinit var controlActivityInterface: ControlActivityInterface

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
        controlActivityInterface.goneNavigationBar()
        return binding.root
    }

    private fun toolbarSetting() {
        binding.toolbar.setupWithNavController(findNavController())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //툴바 설정
        toolbarSetting()

        //운동 기록 리사이클러뷰 데이터 연결
        initMyFitHistoryRecyclerView()

        // 캘린더 연결
        initCalendar()
        controllCalendarMonth()

        //캘린더에서 통신한 운동 기록 데이터를 감시
        observeMyFitHistoryInCalendar()
    }

    fun onClickGuideEnterGroup() {
        val bundle = Bundle()
        bundle.putInt("viewPagerPosition", 1)
        findNavController().navigate(R.id.action_myFitFragment_to_homeFragment, bundle)
    }

    private fun observeMyFitHistoryInCalendar() {
        viewModel.myFitRecordHistory.observe(viewLifecycleOwner) {
            it?.let{
                //TODO 해당 캘린더에 보여지는 월을 구해서 아래 string에 추가해야함.
                val now = binding.calendarView.findFirstVisibleMonth()?.yearMonth
                val month = now?.let{ nowData ->
                    extractNumbers(nowData.month.displayText(false))
                }

                binding.textViewTotalHistoryDateGuide.text = getString(R.string.my_fit_history_date_guide_num, month)
                binding.textViewTotalFitHistoryNum.text = getString(R.string.my_fit_history_total_num, it.size)
                fitHistoryAdapter.submitList(it)
                val fitDateList =  it.map { fitHistoryData ->
                    Instant.parse(fitHistoryData.recordStartDate).atZone(ZoneId.systemDefault()).toLocalDate()
                }
                fitDateList.forEach {
                    binding.calendarView.notifyDayChanged(CalendarDay(it, DayPosition.MonthDate))
                }
            }
        }
    }

    private fun initCalendar() {
        //캘린더 그리는 작업 수행
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                container.textView.text = data.date.dayOfMonth.toString()
                val currentDate = LocalDate.now()  // 현재 날짜 가져오기
                val dayOfWeek = container.day.date.dayOfWeek  // 요일 가져오기

                //만약 해당 월에 존재하는 day이면
                if (data.position == DayPosition.MonthDate) {
                    container.textView.visibility = View.VISIBLE
                    when {
                        container.day.date == currentDate -> {
                            container.view.setBackgroundResource(R.drawable.bg_border_selected) // 원하는 배경 설정
                            container.textView.setTextColor(
                                when (dayOfWeek) {
                                    DayOfWeek.SATURDAY -> requireContext().getColor(R.color.blueGem)
                                    DayOfWeek.SUNDAY -> requireContext().getColor(R.color.red)
                                    else -> requireContext().getColor(R.color.blueGem)
                                }
                            )
                        }
                        else -> {
                            container.textView.setTextColor(
                                when (dayOfWeek) {
                                    DayOfWeek.SATURDAY -> requireContext().getColor(R.color.blueGem)
                                    DayOfWeek.SUNDAY -> requireContext().getColor(R.color.red)
                                    else -> requireContext().getColor(R.color.blueGem)
                                }
                            )
                        }
                    }

                    if (container.day.date == selectedDate) {
                        //선택된 날짜의 상태 변경
                        container.textView.setTextColor(Color.WHITE)
                        container.view.setBackgroundResource(R.drawable.linear_button_gradient)
                    } else {
                        //선택되지 않은 경우
                        if (container.day.date == currentDate) {
                            container.textView.setTextColor(
                                when (dayOfWeek) {
                                    DayOfWeek.SATURDAY -> requireContext().getColor(R.color.blueGem)
                                    DayOfWeek.SUNDAY -> requireContext().getColor(R.color.red)
                                    else -> requireContext().getColor(R.color.blueGem)
                                }
                            )
                            container.view.setBackgroundResource(R.drawable.bg_border_selected) // 원하는 배경 설정
                        } else {
                            container.textView.setTextColor(
                                when (dayOfWeek) {
                                    DayOfWeek.SATURDAY -> requireContext().getColor(R.color.blueGem)
                                    DayOfWeek.SUNDAY -> requireContext().getColor(R.color.red)
                                    else -> Color.BLACK
                                }
                            )
                            container.view.background = null
                        }
                    }
                } else {
                    container.textView.setTextColor(Color.WHITE)
                    container.textView.visibility = View.INVISIBLE
                }

                viewModel.myFitRecordHistory.value?.let{
                    val fitDateList =  it.map { fitHistoryData ->
                        Instant.parse(fitHistoryData.recordStartDate).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    if (fitDateList.contains(container.day.date)){
                        container.dot.visibility = View.VISIBLE
                    }
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


    fun Month.displayText(short: Boolean = true): String {
        val style = if (short) TextStyle.SHORT else TextStyle.FULL
        return getDisplayName(style, Locale.KOREAN)
    }

    private fun updateTitle() {
        val month = binding.calendarView.findFirstVisibleMonth()?.yearMonth ?: return

        val (startDate, endDate) = getStartAndEndInstantsForYearMonth(month.year, extractNumbers(month.month.displayText(false)))

        //유저 아이디 읽어서 통신
        val userPreference = (activity as MainActivity).loadUserPreference()
        val userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        viewModel.getMyFitRecordHistory(userId.toString(), startDate, endDate)//해당 년,월의 운동 기록 통신 시작

        binding.exOneYearText.text = getString(R.string.my_fit_calendar_year_info, month.year)
        binding.exOneMonthText.text = month.month.displayText(short = false)
    }

    fun controllCalendarMonth() {
        binding.exFivePreviousMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let{
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }
        binding.exFiveNextMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let{
                binding.calendarView.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }
    }

    fun extractNumbers(input: String): Int {
        return input.replace(Regex("[^0-9]"), "").toInt()
    }

    fun getStartAndEndInstantsForYearMonth(year: Int, month: Int): Pair<String, String> {
        // 입력된 년도와 월로 LocalDate 객체 생성
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1)

        // 해당 월의 첫 번째 날의 00:00:00과 마지막 날의 23:59:59의 Instant 구하기
        val startInstant =
            firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endInstant =
            lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusSeconds(1)
                .toInstant()
        Log.d("tlqkf",DateParseUtils.instantToString(startInstant))
        Log.d("tlqkf",DateParseUtils.instantToString(endInstant))
        return Pair(DateParseUtils.instantToString(startInstant), DateParseUtils.instantToString(endInstant))
    }


    inner class DayViewContainer(view: View) : ViewContainer(view) {
        // With ViewBinding
        val bindView = CalendarDayLayoutBinding.bind(view)
        val textView = bindView.calendarDayText
        val dot = bindView.CircleImageViewFitDayDot

        lateinit var day: CalendarDay

        init {
            view.setOnClickListener {
                // 해당 월에 유효한 day라면
                if (day.position == DayPosition.MonthDate) {
                    val currentSelection = selectedDate
                    //클릭했던 낳짜를 또 클릭했을 경우
                    if (currentSelection == day.date) {
                        //해당 낳짜의 운동 기록 리사이클러뷰 업데이트(빈 리스트)
                        fitHistoryAdapter.submitList(emptyList())
                        binding.textViewTotalHistoryDateGuide.text = ""
                        binding.textViewTotalFitHistoryNum.text = getString(R.string.my_fit_history_total_num, 0)

                        //선택된 날짜 삭제 후 캘린더에 변경사항 알리기
                        selectedDate = null
                        binding.calendarView.notifyDateChanged(currentSelection)
                    } else { //처음 클릭하는 경우라면
                        //해당 낳짜에 해당하는 운동 기록 리사이클러뷰 업데이트
                        val thatDayFitHistory = viewModel.myFitRecordHistory.value?.filter {
                            it.recordStartDate.contains(day.date.toString())
                        }
                        fitHistoryAdapter.submitList(thatDayFitHistory)
                        binding.textViewTotalHistoryDateGuide.text = getString(R.string.my_fit_history_date_day_guide_num,day.date.monthValue,day.date.dayOfMonth)
                        binding.textViewTotalFitHistoryNum.text = getString(R.string.my_fit_history_total_num, if(thatDayFitHistory.isNullOrEmpty()) 0 else thatDayFitHistory.size )


                        selectedDate = day.date
                        binding.calendarView.notifyDateChanged(day.date)
                        if (currentSelection != null) { //이전에 선택됐던 날짜 표시 삭제
                            binding.calendarView.notifyDateChanged(currentSelection)
                        }
                    }
                }
            }
        }
    }

    inner class MonthViewContainer(view: View) : ViewContainer(view) {
        val titlesContainer = view as ViewGroup
    }
}