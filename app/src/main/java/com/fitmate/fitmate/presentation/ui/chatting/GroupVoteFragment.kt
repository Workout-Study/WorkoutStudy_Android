package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.ChatActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentGroupVoteBinding
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.GroupVoteAdapter
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupVoteFragment : Fragment(R.layout.fragment_group_vote) {

    private lateinit var binding: FragmentGroupVoteBinding
    private val viewModel: VoteViewModel by viewModels()
    private lateinit var voteListAdapter: GroupVoteAdapter
    private var groupId = -1
    private var userId: Int = -1
    private var accessToken: String = ""
    private var platform: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getInt("groupId") ?: -1
        if (groupId != -1) {
            Log.d("woojugoing_group_id", groupId.toString())
        } else {
            Log.d("woojugoing_group_id", groupId.toString())
            Toast.makeText(context, "Error: Group not found!", Toast.LENGTH_SHORT).show()
        }
        loadUserPreference()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupVoteBinding.bind(view)
        binding.materialToolbarGroupVote.setupWithNavController(findNavController())

        //리사이클러뷰 초기화
        settingRecyclerView()

        //탭 레이아웃 설정
        settingTabLayoutFunction()

        //그룹 정보 통신
        getGroupDetail()

        //그룹 정보 통신 결과 감시 후 투표 데이터 통신 시작
        observeGroupDetailAndGetVoteDate()

        //투표 데이터 통신 결과 감시
        observeVoteData()
    }

    private fun settingTabLayoutFunction() {
        binding.voteItemTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        filterVoteDataByTab(0)
                    }

                    1 -> {
                        filterVoteDataByTab(1)
                    }

                    2 -> {
                        filterVoteDataByTab(2)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        filterVoteDataByTab(0)
                    }

                    1 -> {
                        filterVoteDataByTab(1)
                    }

                    2 -> {
                        filterVoteDataByTab(2)
                    }
                }
            }

        })
    }

    private fun filterVoteDataByTab(tabPosition: Int) {
        //통신 데이터가 있다면
        viewModel.fitGroupVotes.value?.let { voteItem ->
            when (tabPosition) {
                0 -> {
                    val filterData = voteItem.fitCertificationDetails.filter {
                        !it.isUserVoteDone
                    }
                    voteListAdapter.submitList(filterData)
                }

                1 -> {
                    val filterData = voteItem.fitCertificationDetails.filter {
                        it.isUserVoteDone
                    }.filter {
                        it.isUserAgree
                    }
                    voteListAdapter.submitList(filterData)
                }

                2 -> {
                    val filterData = voteItem.fitCertificationDetails.filter {
                        it.isUserVoteDone
                    }.filter {
                        !it.isUserAgree
                    }
                    voteListAdapter.submitList(filterData)
                }
            }
        }

    }

    private fun observeVoteData() {
        viewModel.fitGroupVotes.observe(viewLifecycleOwner) { eachFitResponse ->
            stopShimmer()

            //리사이클러뷰 업데이트를 진행하고 현재 내가 진입해있는 탭의 포지션을 재진입 시킴(탭에 맞는 데이터를 submitList하기 위함)
            voteListAdapter.submitList(eachFitResponse.fitCertificationDetails) {
                val tabPosition = binding.voteItemTabLayout.selectedTabPosition
                binding.voteItemTabLayout.selectTab(binding.voteItemTabLayout.getTabAt(tabPosition))
            }
        }
    }

    private fun observeGroupDetailAndGetVoteDate() {
        viewModel.groupDetail.observe(viewLifecycleOwner) { groupDetail ->
            //투표 데이터 통신하기
            viewModel.fetchFitGroupVotes(groupId, userId)
        }
    }

    private fun getGroupDetail() {
        viewModel.getGroupDetail(groupId)
        startShimmer()
    }

    private fun settingRecyclerView() {
        voteListAdapter = GroupVoteAdapter(this, viewModel) { }
        binding.recyclerGroupVote.apply {
            adapter = voteListAdapter
        }
    }

    private fun loadUserPreference() {
        val userPreference = (activity as ChatActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        accessToken = userPreference.getOrNull(0)?.toString() ?: ""
        platform = userPreference.getOrNull(3)?.toString() ?: ""
    }

    private fun startShimmer() {
        binding.groupVoteShimmer.startShimmer()
        binding.groupVoteShimmer.visibility = View.VISIBLE
        binding.recyclerGroupVote.visibility = View.GONE
    }

    private fun stopShimmer() {
        binding.groupVoteShimmer.stopShimmer()
        binding.groupVoteShimmer.visibility = View.GONE
        binding.recyclerGroupVote.visibility = View.VISIBLE
    }

}
