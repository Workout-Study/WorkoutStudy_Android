package com.fitmate.fitmate.presentation.ui.userinfo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentUserInfoBinding
import com.fitmate.fitmate.domain.model.PointType
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.fitmate.fitmate.util.DateParseUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private lateinit var binding: FragmentUserInfoBinding
    private val viewModel: LoginViewModel by viewModels()
    private val TAG = "UserInfoFragment"
    private var userId: Int = -1
    private var accessToken: String = ""
    private var platform: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserInfoBinding.bind(view)
        binding.viewModel = viewModel
        binding.fragment = this

        (activity as ControlActivityInterface).viewNavigationBar()
        loadUserPreference()
        viewModel.getUserInfo(userId)
        viewModel.getPointInfo(userId, "USER")
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.textViewUserInfoName.text = it.nickname
                binding.textViewUserInfoDate.text = getString(R.string.user_info_createdAt,formatDateRange(it.createdAt))
                if (it.imageUrl != null) {
                    Glide.with(binding.imageViewUserInfoIcon.context)
                        .load(it.imageUrl)
                        .transform(CenterCrop(), RoundedCorners(16))
                        .error(R.drawable.ic_launcher_logo)
                        .into(binding.imageViewUserInfoIcon)
                }
            }
        }

        viewModel.pointInfo.observe(viewLifecycleOwner) {
            binding.textViewUserPoint.text =
                getString(R.string.user_info_point, splitAndJoinFromEnd(it.balance.toString()))
        }
    }

    //포인트 ,추가하는 메서드
    private fun splitAndJoinFromEnd(input: String): String {
        val sb = StringBuilder()
        val step = 3
        var i = input.length

        while (i > 0) {
            if (i - step > 0) {
                sb.insert(0, "," + input.substring(i - step, i))
            } else {
                sb.insert(0, input.substring(0, i))
            }
            i -= step
        }

        return sb.toString().trimStart(',')
    }

    private fun formatDateRange(createDate: String): String {
        // 한국 시간대
        val koreaZone = ZoneId.of("Asia/Seoul")

        // 문자열을 Instant로 변환
        val instant = DateParseUtils.stringToInstant(createDate)

        // Instant를 한국 시간대로 변환
        val dateToInstant = instant.atZone(koreaZone).toLocalDateTime()

        // 날짜 및 시간 형식 설정
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd a h:mm")

        // 형식에 맞게 변환
        val dateFormatted = dateToInstant.format(dateFormatter)

        // 결과 문자열 반환
        return dateFormatted
    }


    private fun loadUserPreference() {
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        accessToken = userPreference.getOrNull(0)?.toString() ?: ""
        platform = userPreference.getOrNull(3)?.toString() ?: ""
        Log.d(TAG, "$userId, $platform")
        Log.d(TAG, accessToken)
    }

    //프로필 변경 화면으로 이동하는 메서드
    fun navigateProfile() {
        val bundle = Bundle().apply {
            viewModel.userInfo.value?.let { userInfoData ->
                putSerializable("userInfoData", userInfoData)
            }
        }
        findNavController().navigate(R.id.profileFragment, bundle)
    }

    //알림 화면으로 이동하는 메서드
    fun notice() {
        Toast.makeText(context, "지정된 공지사항이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }

    //로그아웃 시도  메서드
    fun logout() {
        viewModel.logoutComplete.observe(viewLifecycleOwner) { isComplete ->
            if (isComplete) {
                findNavController().navigate(R.id.action_userInfoFragment_to_loginFragment)
                Snackbar.make(
                    requireView(),
                    "로그아웃을 성공했습니다. [USERID ${userId}]",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        when (platform) {
            "kakao" -> viewModel.logout(accessToken, "kakao")
            "naver" -> viewModel.logout(accessToken, "naver")
        }
    }

    //회원 탈퇴 메서드
    fun withdraw() {
        viewModel.deleteUser(userId)
        Snackbar.make(binding.root, "회원틸퇴를 성공했습니다. [USERID ${userId}]", Snackbar.LENGTH_SHORT)
            .show()
    }

    //라이선스 화면 이동 메서드
    fun navigateLicense() {
        findNavController().navigate(R.id.action_userInfoFragment_to_licenseFragment)
    }

    //피틀오프 화면으로 이동하는 메서드
    fun navigateFitOff() {
        val bundle = Bundle().apply {
            viewModel.userInfo.value?.let { userInfoData ->
                putSerializable("fitOffOwnerNameInfo", userInfoData)
            }
        }
        findNavController().navigate(R.id.viewFitOffFragment, bundle)
    }

    //포인트 화면으로 이동하는 메서드
    fun navigatePoint() {
        val bundle = Bundle().apply {
            putSerializable("pointOwnerType", PointType.USER)
        }
        findNavController().navigate(R.id.pointFragment, bundle)
    }

}
