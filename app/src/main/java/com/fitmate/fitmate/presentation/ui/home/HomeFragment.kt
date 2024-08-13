package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentHomeBinding
import com.fitmate.fitmate.presentation.ui.home.list.adapter.CarouselAdapter
import com.fitmate.fitmate.presentation.ui.home.list.adapter.MyGroupNewsAdapter
import com.fitmate.fitmate.presentation.viewmodel.HomeViewModel
import com.fitmate.fitmate.util.PendingTokenValue
import com.fitmate.fitmate.util.customGetSerializable
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private var pendingTokenValue: PendingTokenValue? = null
    private val viewModel: HomeViewModel by viewModels()
    lateinit var myGroupNewsAdapter: MyGroupNewsAdapter
    private var userId: Int = -1
    private var accessToken: String = ""
    private var refreshToken: String = ""
    private var platform: String = ""
    private var createdAt: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //펜딩 인텐트로 앱이 시작되었을 경우 네비게이션 작업 수행
        arguments?.let {
            it.customGetSerializable<PendingTokenValue>("pendingToken")?.let {
                pendingTokenValue = it
            }
        }
        when(pendingTokenValue){
            PendingTokenValue.CERTIFICATION -> {
                findNavController().navigate(R.id.action_homeFragment_to_certificateFragment)
            }
            else -> {}
        }

        //로컬에 저장된 데이터 가져오기
        loadUserPreference()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        //바텀 네비 보이도록 설정
        (activity as MainActivity).viewNavigationBar()

        //리사이클러뷰 설정
        initRecyclerView()
        //setCarousel()
        
        //네트워킹 감시
        observeViewModel()

        //내가 가입한 그룹 네트워킹
        viewModel.getMyFitGroupList(userId)


        startShimmer()
    }


    private fun initRecyclerView() {
        myGroupNewsAdapter = MyGroupNewsAdapter(this,viewModel) {}

        myGroupNewsAdapter.addOnPagesUpdatedListener {
            stopShimmer()
            if (myGroupNewsAdapter.itemCount == 0){
                binding.textViewHomeNoGroup.visibility = View.VISIBLE
            }else{
                binding.textViewHomeNoGroup.visibility = View.GONE
            }
        }
        binding.recyclerViewMyGroupNews.apply {
            adapter = myGroupNewsAdapter
        }
    }



    private fun observeViewModel() {
        viewModel.run {
            viewModelScope.launch {
                myFitGroupList.observe(viewLifecycleOwner) { groupList ->
                    //그룹 소식 네트워킹
                    viewModel.getPagingGroupNews(userId,0, 10)
                }
            }

            viewModelScope.launch {
                pagingData.collectLatest {
                    if (it != null){
                        myGroupNewsAdapter.submitData(lifecycle, it)
                    }
                }
            }
        }
    }

    private fun startShimmer() {
        binding.homeShimmer.startShimmer()
        binding.homeShimmer.visibility = View.VISIBLE
    }

    private fun stopShimmer() {
        binding.homeShimmer.stopShimmer()
        binding.homeShimmer.visibility = View.GONE
    }

    private fun loadUserPreference() {
        val userPreference = (activity as MainActivity).loadUserPreference()
        accessToken = userPreference.getOrNull(0)?.toString() ?: ""
        refreshToken = userPreference.getOrNull(1)?.toString() ?: ""
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        platform = userPreference.getOrNull(3)?.toString() ?: ""
        createdAt = userPreference.getOrNull(4)?.toString() ?: ""
        Log.d("woojugoing", "$userId, $platform")
        Log.d("woojugoing", accessToken)
        Log.d("woojugoing", createdAt)
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
}
