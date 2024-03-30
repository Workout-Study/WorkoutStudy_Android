package com.fitmate.fitmate.presentation.ui.certificate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.databinding.FragmentCertificateBinding
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.presentation.ui.certificate.list.adapter.CertificationImageAdapter
import com.fitmate.fitmate.presentation.viewmodel.CertificateState
import com.fitmate.fitmate.presentation.viewmodel.CertificationViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CertificateFragment : Fragment() {
    companion object {
        //사진 최대 선택 가능 갯수
        const val IMAGE_PICK_MAX = 5
    }
    
    private lateinit var binding: FragmentCertificateBinding
    private lateinit var controlActivityInterface: ControlActivityInterface
    private lateinit var certificationImageAdapter: CertificationImageAdapter
    private val viewModel: CertificationViewModel by viewModels()
    private var pickMultipleMedia = activityResultLauncher()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //초기 설정
        binding = FragmentCertificateBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.materialToolbarCertificate.setupWithNavController(findNavController())

        //바텀 네비 삭제.
        removeBottomNavi()

        //리사이클러뷰 어뎁터 초기화
        initRecyclerviewAdapter()

        //TODO 초기에 room의 데이터에 따라서 상태를 설정한다
        viewModel.setStateCertificateNonProceeding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //스타트 사진 이미지 첨부 버튼(ImageView) 설정
        setStartImageAddButtonClick()

        //제출버튼 클릭 리스너
        binding.buttonCertificateConfirm.setOnClickListener {

        }

        //기록 시작 이미지 첨부 감시
        viewModel.startImageList.observe(viewLifecycleOwner) {
            //TODO 상태 설정도 동시에 해줘야함.
            certificationImageAdapter.submitList(it)
            certificationImageAdapter.notifyDataSetChanged()
            if (it.isEmpty()) {
                viewModel.setStateCertificateNonProceeding()
            } else {
                viewModel.setStateCertificateAddedStartImage()
            }
        }

        //화면 상태 설정을 위한 state 감시
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                CertificateState.PROCEEDING -> {
                    setRecyclerViewState()
                }

                CertificateState.ADDED_START_IMAGE -> {
                    binding.buttonCertificateConfirm.setOnClickListener {
                        //TODO 여기서 인증 시작 로직(서비스, Room에 데이터 저장)이 이루어저야함
                        viewModel.setStateCertificateProceed()
                    }
                }
                CertificateState.NON_PROCEEDING->{

                }
                else->{}
            }
        }
    }


    //바텀 네비 삭제 메서드
    private fun removeBottomNavi() {
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.goneNavigationBar()
    }

    //시작, 끝 사진 첨부 리사이클러뷰 어댑터 초기화 및 연결 메서드
    private fun initRecyclerviewAdapter() {
        certificationImageAdapter = CertificationImageAdapter { itemPosition ->
            viewModel.removeStartImage(itemPosition)
            certificationImageAdapter.notifyItemRemoved(itemPosition)
        }

        binding.recyclerCertificateStartImage.apply {
            adapter = certificationImageAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setStartImageAddButtonClick() {
        binding.imageViewFitStartSelectImage.setOnClickListener {
            if ((viewModel.startImageList.value?.size ?: 0) >= IMAGE_PICK_MAX) {
                return@setOnClickListener
            }
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
    }

    private fun activityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                if (uris.size > IMAGE_PICK_MAX - (viewModel.startImageList.value?.size ?: 0)) {
                    Toast.makeText(requireContext(), "사진은 최대 5장까지 첨부할 수 있습니다!", Toast.LENGTH_SHORT)
                        .show()
                    return@registerForActivityResult
                }
                uris.forEach {
                    val image = CertificationImage(it)
                    viewModel.addStartImage(image)
                }
            }
        }

    //시작 사진 첨부 수정 불가능하도록 설정하는 메서드
    private fun setRecyclerViewState() {
        certificationImageAdapter.changeVisible()
        binding.cardViewItemCertificateStart.visibility = View.GONE
    }
}