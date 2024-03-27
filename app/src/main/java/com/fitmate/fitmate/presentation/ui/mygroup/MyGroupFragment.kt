package com.fitmate.fitmate.presentation.ui.mygroup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.ChatActivity
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMyGroupBinding
import com.fitmate.fitmate.domain.model.FitGroup
import com.fitmate.fitmate.ui.myfit.list.adapter.MyFitGroupAdapter
import com.fitmate.fitmate.util.ControlActivityInterface

class MyGroupFragment: Fragment(R.layout.fragment_my_group) {

    private lateinit var binding: FragmentMyGroupBinding

    private lateinit var adapter: MyFitGroupAdapter

    private lateinit var controlActivityInterface: ControlActivityInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyGroupBinding.inflate(layoutInflater)
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.viewNavigationBar()

        adapter = MyFitGroupAdapter { data->
            startActivity(Intent(requireContext(),ChatActivity::class.java))

        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingButtonMakeFitGroup.setOnClickListener {
            findNavController().navigate(R.id.makeGroupFragment)
        }
        //채팅방 어뎁터 초기화 및 목데이터 연결작업
        initListAdapter()

    }

    private fun initListAdapter() {
        val mockDataList = listOf<FitGroup>(
            FitGroup("축구에 미친자들", "오후 3:24", "오늘 진짜 침드네요"),
            FitGroup("닥치고 스쾉", "오후 5:24", "오늘 3대 650 찍었습니다 ㄷㄷ"),
            FitGroup("다이어트 방", "오후 1:24", "그냥 포기할게요")
        )
        adapter.submitList(mockDataList){
            if (mockDataList.isNotEmpty()){
                binding.textViewMyFitGroupEmpty.visibility = View.GONE
            }else{
                binding.textViewMyFitGroupEmpty.visibility = View.VISIBLE
            }
        }
        binding.recyclerViewMyFitGroupList.adapter = adapter
    }
}