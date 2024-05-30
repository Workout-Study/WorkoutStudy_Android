package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.VoteCertification
import com.fitmate.fitmate.databinding.FragmentHomeBinding
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.home.list.adapter.CarouselAdapter
import com.fitmate.fitmate.presentation.ui.home.list.adapter.VoteAdapter
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: VoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchMyFitGroupVotes(1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).viewNavigationBar()
        initView(view)
        setCarousel()
        observeViewModel()
    }


    private fun initView(view: View) {
        binding = FragmentHomeBinding.bind(view)
        recyclerView = binding.recyclerViewHomeMain
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = VoteAdapter(this) {}
    }

    private fun setCarousel(){
        val carouselView: RecyclerView = binding.recyclerViewHomeCarousel.apply {
            layoutManager = CarouselLayoutManager(MultiBrowseCarouselStrategy())    // TODO 나중에 Carousel의 방식을 추후 이미지 적용 후 변경해봐야 함.
        }

        val imageUrls = listOf(
            "https://scontent-ssn1-1.xx.fbcdn.net/v/t1.6435-9/118785784_3128783457234268_5350155294171284268_n.jpg?stp=dst-jpg_p600x600&_nc_cat=105&ccb=1-7&_nc_sid=5f2048&_nc_ohc=KlX3sdWkYZEAb6ho-Rs&_nc_ht=scontent-ssn1-1.xx&oh=00_AfBP6Ii-VR3bYD81PQvGBV0gxsDaIVPkMLs4BKMLiP532Q&oe=664A9DF8",
            "https://img.sbs.co.kr/newimg/news/20221219/201732615_500.jpg",
            "https://news.seoul.go.kr/env/files/2021/06/1KakaoTalk_20210601_190713975.png",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQj9Che43hJa0kWL-Z1-zHZycqccPbaHuKZxvPfQ9Xg1Q&s",
            "https://health.chosun.com/site/data/img_dir/2022/07/08/2022070801727_0.jpg"
        )

        val carouselAdapter = CarouselAdapter(imageUrls) {
            val bundle = Bundle()
            bundle.putInt("groupId", 10)
            findNavController().navigate(R.id.action_homeMainFragment_to_groupJoinFragment, bundle)
        }
        carouselView.adapter = carouselAdapter
    }

    private fun observeViewModel() {
        startShimmer()
        viewModel.myGroupVotes.observe(viewLifecycleOwner) { result ->
            stopShimmer()
            result.onSuccess { groups ->
                val voteItems = groups.map { group ->
                    group.certificationList.map { cert ->
                        VoteItem(
                            title = group.groupName, fitMate = cert.requestUserId,
                            percent = formatPercent(cert), time = formatDate(cert),
                            image = cert.multiMediaEndPoints.firstOrNull() ?: "",
                            groupId = group.groupId, startTime = null, endTime = null
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
