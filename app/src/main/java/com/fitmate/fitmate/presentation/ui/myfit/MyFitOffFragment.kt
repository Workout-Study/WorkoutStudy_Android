package com.fitmate.fitmate.ui.myfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.databinding.FragmentMyFitOffBinding
import com.fitmate.fitmate.domain.model.MyFitOff
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitOffAdapter
import com.fitmate.fitmate.util.ControlActivityInterface

class MyFitOffFragment: Fragment() {
    private lateinit var binding: FragmentMyFitOffBinding
    private lateinit var controlActivityInterface: ControlActivityInterface

    private val adapter: MyFitOffAdapter by lazy { MyFitOffAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyFitOffBinding.inflate(layoutInflater)
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.goneNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.materialToolbarMyFitOff.setupWithNavController(findNavController())

        //리사이클러뷰 mock 데이터 연결
        initMockData()

    }

    private fun initMockData() {
        val mockData = listOf<MyFitOff>(
            MyFitOff(
                title = "축구를 좋아하는 사람들",
                reason = "축구하다가 다리 인대가 끊어짐"
            ),
            MyFitOff(
                title = "닥치고 스쾉",
                reason = "축구하다가 다리 인대가 끊어짐"
            )

        )

        adapter.submitList(mockData) {
            if (mockData.isNotEmpty()) {
                binding.textViewFitOffDataEmpty.visibility = View.GONE
            }else{
                binding.textViewFitOffDataEmpty.visibility = View.VISIBLE
            }
            binding.recyclerMyFitOff.adapter = adapter

        }
    }
}