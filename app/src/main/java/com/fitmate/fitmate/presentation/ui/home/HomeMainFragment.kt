package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.VoteCertification
import com.fitmate.fitmate.databinding.FragmentHomeMainBinding
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.home.list.adapter.CarouselAdapter
import com.fitmate.fitmate.presentation.ui.home.list.adapter.VoteAdapter
import com.fitmate.fitmate.presentation.viewmodel.HomeMainViewModel
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselStrategy
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeMainFragment : Fragment(R.layout.fragment_home_main) {

    private lateinit var binding: FragmentHomeMainBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: HomeMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchMyFitGroupVotes()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setCarousel(MultiBrowseCarouselStrategy())
        observeViewModel()
    }


    private fun initView(view: View) {
        binding = FragmentHomeMainBinding.bind(view)
        recyclerView = binding.recyclerViewHomeMain
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = VoteAdapter(this) {}
    }

    private fun setCarousel(carouselStrategy: CarouselStrategy){
        val carouselView: RecyclerView = binding.recyclerViewHomeCarousel.apply {
            layoutManager = CarouselLayoutManager(carouselStrategy)    // TODO 나중에 Carousel의 방식을 추후 이미지 적용 후 변경해봐야 함.
        }

        val carouselAdapter = CarouselAdapter {
            findNavController().navigate(R.id.action_homeFragment_to_groupJoinFragment)
        }
        carouselView.adapter = carouselAdapter
    }

    private fun observeViewModel() {
        startShimmer()
        viewModel.fitGroupVotes.observe(viewLifecycleOwner) { result ->
            stopShimmer()
            result.onSuccess { groups ->
                val voteItems = groups.map { group ->
                    group.certificationList.map { cert ->
                        VoteItem(
                            title = group.groupName,
                            fitMate = cert.requestUserId,
                            percent = formatPercent(cert),
                            time = formatDate(cert),
                            image = cert.multiMediaEndPoints.firstOrNull() ?: "",
                            groupId = group.groupId
                        )}}.flatten().distinctBy { it.title + it.fitMate + it.time }
                val voteAdapter = binding.recyclerViewHomeMain.adapter as VoteAdapter
                voteAdapter.submitList(voteItems)
            }
        }
    }

    private fun startShimmer() {
        binding.homeShimmer.startShimmer()
        binding.homeShimmer.visibility = View.VISIBLE
        binding.recyclerViewHomeMain.visibility = View.GONE
    }

    private fun stopShimmer() {
        binding.homeShimmer.stopShimmer()
        binding.homeShimmer.visibility = View.GONE
        binding.recyclerViewHomeMain.visibility = View.VISIBLE
    }

    private fun formatPercent(cert: VoteCertification): Int {
        return if (cert.agreeCount + cert.disagreeCount > 0) (cert.agreeCount * 100) / (cert.agreeCount + cert.disagreeCount) else 0
    }

    private fun formatDate(cert: VoteCertification): String {
        return ZonedDateTime.parse(cert.voteEndDate).format(
            DateTimeFormatter.ofPattern("(MM/dd) HH:mm 종료")
        )
    }
}
