package com.fitmate.fitmate.ui.myfit

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.database.getStringOrNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.databinding.FragmentCertificateBinding
import com.fitmate.fitmate.domain.usecase.DbCertificationUseCase
import com.fitmate.fitmate.presentation.viewmodel.CertificationViewModel
import com.fitmate.fitmate.presentation.viewmodel.GalleryImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class CertificateFragment: Fragment() {
    companion object{
        const val GALLERY_REQUEST_CODE = 100
        const val CAMERA_REQUEST_CODE = 101
        const val DEFAULT_GALLERY_REQUEST_CODE = 2
    }
    @Inject
    lateinit var dbCertificationUseCase: DbCertificationUseCase
    private lateinit var binding: FragmentCertificateBinding
    private lateinit var mainActivity:MainActivity
    private lateinit var certificationViewModel: CertificationViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCertificateBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        certificationViewModel = ViewModelProvider(this)[CertificationViewModel::class.java]
        binding.imageViewFitStartSelectImage.setOnClickListener {

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.materialToolbarCertificate.setupWithNavController(findNavController())


    }

    // 갤러리에서 이미지 선택
    private fun pickImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    // 카메라 실행
    private fun captureImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 갤러리에서 선택한 이미지 처리
                    val selectedImageUri: Uri? = data?.data
                    // ...
                }
            }
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 카메라로 촬영한 이미지 처리
                    val capturedImage: Bitmap? = data?.extras?.get("data") as? Bitmap
                    // ...
                }
            }
        }
    }*/



}