package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMakeFitOffBinding
import com.fitmate.fitmate.domain.model.FitOffRequest
import com.fitmate.fitmate.presentation.viewmodel.FitOffViewModel
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import com.fitmate.fitmate.util.DateParseUtils
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MakeFitOffFragment : Fragment(R.layout.fragment_make_fit_off) {

    private lateinit var binding: FragmentMakeFitOffBinding
    private val viewModel: FitOffViewModel by viewModels()
    private var userId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMakeFitOffBinding.bind(view)
        binding.fragment = this
        binding.viewModel = viewModel

        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1

        viewModel.registerFitOffResponse.observe(viewLifecycleOwner) { response ->
            if (response.fitOffId != null){
                loadingEnd()
                Toast.makeText(requireContext(),"피트오프 신청을 완료했습니다",Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(requireContext(),"피트오프 신청을 실패했습니다!",Toast.LENGTH_SHORT).show()
                loadingEnd()
            }

        }

    }

    fun onClickSelectDateRange() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.make_fit_off_scr_select_date))
                .build()

        dateRangePicker.show(childFragmentManager, "date_picker")
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            calendar.timeInMillis = selection?.first ?: 0
            val startDate = dateFormat.format(calendar.time).toString()
            Log.d("start", startDate)

            calendar.timeInMillis = selection?.second ?: 0
            val endDate = dateFormat.format(calendar.time).toString()
            Log.d("end", endDate)

            binding.textViewDateRange.setText("$startDate ~ $endDate")
        }
    }

    fun onClickConfirmFitOff() {
        if (viewModel.fitOffDateRange.value.isNullOrEmpty()) { // 기간을 설정하지 않은 경우
            Toast.makeText(requireContext(), "피트 오프 신청 기간을 선택해주세요!", Toast.LENGTH_SHORT).show()
        } else if (viewModel.fitOffReason.value.isNullOrBlank()) { // 사유를 입력하지 않은 경우
            Toast.makeText(requireContext(), "피트 오프 사유를 입력해주세요!", Toast.LENGTH_SHORT).show()
        } else { //모든 사항을 입력했을 경우
            val subStringStartAndEndDate = extractDateRange(viewModel.fitOffDateRange.value!!)
            val fitOffStartDate: String? = subStringStartAndEndDate?.first
            val fitOffEndDate: String? = subStringStartAndEndDate?.second

            if (!fitOffStartDate.isNullOrBlank() && !fitOffEndDate.isNullOrBlank()) {
                val instantFitOffDate = convertToDateTime(fitOffStartDate, fitOffEndDate)
                if (DateParseUtils.stringToInstant(instantFitOffDate.first).isAfter(Instant.now())) { //선택한 기간이 오늘보다 이전인지 확인하는 작업
                    loadingStart()
                    val requestItem = FitOffRequest(requestUserId = userId, fitOffStartDate = instantFitOffDate.first, fitOffEndDate = instantFitOffDate.second, viewModel.fitOffReason.value!!)
                    viewModel.registerFitOff(requestItem)
                } else { //시작 날짜가 현재 시점보다 이전이라면
                    Toast.makeText(requireContext(), "피트오프 시작 기간이 오늘보다 이전을 선택할 수 없습니다!", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun loadingStart() {
        binding.loadingLayoutView.visibility = View.VISIBLE
        binding.progressBarSubmitLoading.visibility = View.VISIBLE
    }

    private fun loadingEnd() {
        binding.loadingLayoutView.visibility = View.GONE
        binding.progressBarSubmitLoading.visibility = View.GONE
    }


    private fun extractDateRange(dateRange: String): Pair<String, String>? {
        val datePattern = Regex("""(\d{4}-\d{2}-\d{2}) ~ (\d{4}-\d{2}-\d{2})""")
        val matchResult = datePattern.find(dateRange)

        return if (matchResult != null && matchResult.groupValues.size == 3) {
            val startDate = matchResult.groupValues[1]
            val endDate = matchResult.groupValues[2]
            Pair(startDate, endDate)
        } else {
            null
        }
    }

    private fun convertToDateTime(startDate: String, endDate: String): Pair<String, String> {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSXXX", Locale.getDefault())

        // Parse the input date strings
        val startDateParsed = inputFormat.parse(startDate)
        val endDateParsed = inputFormat.parse(endDate)

        // Set end date time to 23:59:59.999
        val calendar = Calendar.getInstance()
        calendar.time = endDateParsed
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDateWithTime = calendar.time

        // Format the parsed dates into the desired output format
        val startDateTime = outputFormat.format(startDateParsed)
        val endDateTime = outputFormat.format(endDateWithTime)

        return Pair(startDateTime, endDateTime)
    }



}