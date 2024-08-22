package com.fitmate.fitmate.presentation.ui.dialog.simple

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.DialogSimpleBinding

class SimpleDialog<T, U>(
    private val simpleDialogInterface: T,
    private val titleText: String,
    private val data: U,
): DialogFragment() {

    private var _binding: DialogSimpleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Fitmate_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSimpleBinding.inflate(layoutInflater)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.apply {
            textViewSimpleDialogTitle.text = titleText
            cancelButton.text = "아니오"
            positiveButton.text = "네"

            binding.cancelButton.setOnClickListener {
                dismiss()
            }

            binding.positiveButton.setOnClickListener {
                (simpleDialogInterface as SimpleDialogInterface).onDialogPositiveButtonClick(data)
                dismiss()
            }

        }

        return binding.root
    }

}