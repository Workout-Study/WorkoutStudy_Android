package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentHomeMainBinding
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.home.list.adapter.CarouselAdapter
import com.fitmate.fitmate.presentation.ui.home.list.adapter.VoteAdapter
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselStrategy
import com.google.android.material.carousel.MultiBrowseCarouselStrategy

class HomeMainFragment : Fragment() {

    private lateinit var binding: FragmentHomeMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMainBinding.inflate(inflater, container, false)

        setCarousel(MultiBrowseCarouselStrategy())
        setVote()

        return binding.root
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

    private fun setVote() {
        val recyclerView: RecyclerView = binding.recyclerViewHomeMain
        val voteAdapter = VoteAdapter { _ -> findNavController().navigate(R.id.groupVoteFragment2) }
        val testItems = listOf(VoteItem(title = "Test FitGroup", fitmate = "김성호", percent = 3, time = 5))

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = voteAdapter
        voteAdapter.submitList(testItems)
    }

}
