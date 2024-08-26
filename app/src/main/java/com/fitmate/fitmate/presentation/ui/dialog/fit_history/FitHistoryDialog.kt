package com.fitmate.fitmate.presentation.ui.dialog.fit_history

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.DialogFitHistoryDetailBinding
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail
import com.fitmate.fitmate.util.DateParseUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class FitHistoryDialog(
    viewBinding: DialogFitHistoryDetailBinding,
    data: MyFitRecordHistoryDetail
):DialogFragment() {
    private var _binding: DialogFitHistoryDetailBinding? = null
    private val binding get() = _binding!!
    private val itemData: MyFitRecordHistoryDetail

    init {
        _binding = viewBinding
        itemData = data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitmate_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //배경 색 변경
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.apply {
            data = itemData
            dialog = this@FitHistoryDialog
            recyclerViewFitHistoryImage.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerViewFitHistoryImage.adapter = FitHistoryImageAdapter(itemData.multiMediaEndPoints)

                    //그룹 리스트 addView
            itemData.registeredFitCertifications.forEach {
                val textView: TextView = TextView(requireContext())
                textView.setTextAppearance(R.style.Font_regular12)
                textView.text = it.fitGroupName
                layoutTargetFitGroup.addView(textView)
                }

            buttonOk.setOnClickListener {
                dismiss()
            }

        }

        return binding.root
    }

    fun formatDateRange(startDate: String, endDate: String): String {
        // 한국 시간대
        val koreaZone = ZoneId.of("Asia/Seoul")

        //한국 시간으로 변환
        val startDateToInstant =
            LocalDateTime.ofInstant(DateParseUtils.stringToInstant(startDate), koreaZone)
        val endDateToInstant =
            LocalDateTime.ofInstant(DateParseUtils.stringToInstant(endDate), koreaZone)

        // 날짜 및 시간 형식 설정
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd a h:mm")
        val timeFormatter = DateTimeFormatter.ofPattern("a h:mm")

        // 형식에 맞게 변환
        val startFormatted = startDateToInstant.format(dateFormatter)
        val endFormatted = endDateToInstant.format(timeFormatter)

        // 결과 문자열 반환
        return "$startFormatted ~ $endFormatted"
    }
}

class FitHistoryImageAdapter(private val imageUrls: List<String>?) : RecyclerView.Adapter<FitHistoryImageAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fit_history_image_detail, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        imageUrls?.let {
            Glide.with(holder.itemView.context).load(imageUrls[position]).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        imageUrls?.let {
            return imageUrls.size
        }
        return 0
    }
}
