package com.fitmate.fitmate.presentation.ui.dialog.simple

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.fitmate.fitmate.databinding.DialogSimpleBinding

class SimpleDialog<T : SimpleDialogInterface, U : Any>(
    private val simpleDialogInterface: T,
    private val titleText: String,
    private val data: U,
): DialogFragment() {

    private var _binding: DialogSimpleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSimpleBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.apply {
            textViewSimpleDialogTitle.text = titleText
            cancelButton.text = "아니오"
            positiveButton.text = "네"

            binding.cancelButton.setOnClickListener {
                dismiss()
            }

            binding.positiveButton.setOnClickListener {
                simpleDialogInterface.onDialogPositiveButtonClick(data)
                dismiss()
            }

        }

        return binding.root
    }

}