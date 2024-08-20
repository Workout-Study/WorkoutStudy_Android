package com.fitmate.fitmate.presentation.ui.fitoff

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.databinding.FragmentViewFitOffBinding
import com.fitmate.fitmate.presentation.ui.fitoff.list.adapter.MyFitOffAdapter
import com.fitmate.fitmate.presentation.viewmodel.FitOffViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.fitmate.fitmate.util.customGetSerializable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewFitOffFragment: Fragment() {
    private lateinit var binding: FragmentViewFitOffBinding
    private lateinit var controlActivityInterface: ControlActivityInterface
    private val viewModel: FitOffViewModel by viewModels()
    private lateinit var fitOffOwnerNameInfo: Any
    private val adapter: MyFitOffAdapter by lazy { MyFitOffAdapter(this, fitOffOwnerNameInfo) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            it.customGetSerializable<GetFitMateList>("fitOffOwnerNameInfo")?.let{
                fitOffOwnerNameInfo = it
            }
            it.customGetSerializable<UserResponse>("fitOffOwnerNameInfo")?.let{
                fitOffOwnerNameInfo = it
            }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewFitOffBinding.inflate(layoutInflater)
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.goneNavigationBar()
        //피트오프 데이터 감시
        observeFitOffData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.materialToolbarMyFitOff.setupWithNavController(findNavController())
        binding.recyclerMyFitOff.adapter = adapter

        //피트오프 통신
        networkFitOffData()

    }

    private fun observeFitOffData() {
        viewModel.fitOffResponse.observe(viewLifecycleOwner) {
            adapter.submitList(it.content)
            if (it.content.isEmpty()) {
                binding.recyclerMyFitOff.visibility = View.GONE
                binding.textViewFitOffDataEmpty.visibility = View.VISIBLE
            } else {
                binding.recyclerMyFitOff.visibility = View.VISIBLE
                binding.textViewFitOffDataEmpty.visibility = View.GONE
            }
        }
    }

    private fun networkFitOffData() {
        when (fitOffOwnerNameInfo) {
            is GetFitMateList -> {
                binding.materialToolbarMyFitOff.title = getString(R.string.group_fit_off_scr_toolbar)
                viewModel.getFitOffByGroupId((fitOffOwnerNameInfo as GetFitMateList).fitGroupId)
            }

            is UserResponse -> {
                binding.materialToolbarMyFitOff.title = getString(R.string.my_fit_off_scr_toolbar)
                viewModel.getFitOffByUserId((fitOffOwnerNameInfo as UserResponse).userId)
            }

            else -> {
            }
        }
    }
}