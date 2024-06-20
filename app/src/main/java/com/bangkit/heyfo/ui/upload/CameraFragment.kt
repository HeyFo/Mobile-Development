package com.bangkit.heyfo.ui.upload

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bangkit.heyfo.R
import com.bangkit.heyfo.SharedPreferences
import com.bangkit.heyfo.data.response.PredictResponse
import com.bangkit.heyfo.data.retrofit.ApiConfig
import com.bangkit.heyfo.databinding.FragmentCameraBinding
import com.bangkit.heyfo.ui.list.ListActivity
import com.bangkit.heyfo.ui.upload.camerax.CameraActivity
import com.bangkit.heyfo.ui.upload.camerax.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.IOException

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private val CAMERA_REQUEST_CODE = 100

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CameraActivity.CAMERAX_RESULT) {
            val imageUriString = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
            imageUriString?.let {
                currentImageUri = Uri.parse(it)
                showImage()
            }
        } else {
            Toast.makeText(requireContext(), "Gambar tidak tersimpan.", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            progressIndicator
            previewImageView
            cameraButton
            galleryButton
            uploadButton
        }

        binding.cameraButton.setOnClickListener {
            checkCameraPermission()
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.uploadButton.setOnClickListener {
            uploadImage()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            startCameraActivity()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity()
            } else {
                Toast.makeText(requireContext(), "Kamera dibutuhkan untuk mengambil gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCameraActivity() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        cameraActivityResultLauncher.launch(intent)
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            try {
                val imageFile = uriToFile(uri, requireContext())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestImageFile
                )

                lifecycleScope.launch {
                    showLoading(true)
                    try {
                        val apiService = ApiConfig.getApiService()
                        val response = withContext(Dispatchers.IO) { apiService.getPredict(multipartBody) }
                        if (response.isSuccessful) {
                            val successResponse = response.body()
                            successResponse?.let {
                                val predictList = it.data?.filterNotNull() ?: listOf()
                                val predictPreferences = SharedPreferences()
                                predictPreferences.savePredictList(requireContext(), predictList)

                                // Get the first prediction UUID
                                val firstPredictionUUID = predictList.firstOrNull()?.uuid

                                val intent = Intent(requireContext(), ListActivity::class.java).apply {
                                    putExtra("uuid", firstPredictionUUID)
                                }
                                startActivity(intent)
                            } ?: showToast("Gambar berhasil diunggah, tapi tidak ada hasil prediksi.")
                        } else {
                            val errorBody = response.errorBody()?.string()
                            val errorResponse = Gson().fromJson(errorBody, PredictResponse::class.java)
                            val errorMessage = errorResponse.predictResult?.joinToString() ?: "Gagal mengunggah gambar."
                            showToast(errorMessage)
                        }
                    } catch (e: Exception) {
                        Log.e("Upload Error", e.message ?: "Error tidak diketahui")
                        showToast("Gagal mengunggah gambar. Coba lagi nanti.")
                    } finally {
                        showLoading(false)
                    }
                }
            } catch (e: IOException) {
                showToast("Gagal mengakses gambar.")
                Log.e("File Error", e.message ?: "Error tidak diketahui")
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
