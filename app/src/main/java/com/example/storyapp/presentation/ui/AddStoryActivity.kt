package com.example.storyapp.presentation.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.storyapp.MainActivity
import com.example.storyapp.R
import com.example.storyapp.core.util.Internet
import com.example.storyapp.core.util.Result
import com.example.storyapp.core.util.UriTo
import com.example.storyapp.core.util.UriTo.Companion.rotateBitmap
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.presentation.view_model.StoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {
    private val storyViewModel: StoryViewModel by viewModels()
    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.cant_get_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        val token = intent.getStringExtra("token")


        binding.apply {
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            btnUpload.setOnClickListener {
                if (Internet.isAvailable(this@AddStoryActivity)) {
                    if (etDescription.text.isNullOrBlank()) {
                        etDescription.error = resources.getString(R.string.required)
                        etDescription.requestFocus()
                    } else {
                        if (etDescription.error.isNullOrEmpty()) {
                            token?.let {
                                addStory(it)
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        resources.getString(R.string.no_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            getFile = UriTo.file(selectedImg, this@AddStoryActivity)
            binding.ivPreviewImage.setImageURI(selectedImg)
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            getFile = myFile

            binding.ivPreviewImage.setImageBitmap(result)
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun addStory(token: String) {
        getFile?.let {
            reduceFileImage(it)
            val description =
                binding.etDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                it.name,
                requestImageFile
            )
            storyViewModel.addStory("Bearer $token", imageMultipart, description)
                .observe(this@AddStoryActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            Log.d(TAG, "loading...")
                            binding.apply {
                                cButton.visibility = View.INVISIBLE
                                pbButton.visibility = View.VISIBLE
                            }
                        }
                        is Result.Success -> {
                            Toast.makeText(
                                this@AddStoryActivity,
                                resources.getString(R.string.story_created),
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@AddStoryActivity, MainActivity::class.java))
                            finish()
                            Log.d(TAG, result.data?.message.toString())
                        }
                        is Result.Error -> {
                            binding.apply {
                                cButton.visibility = View.VISIBLE
                                pbButton.visibility = View.GONE
                            }
                            Log.d(TAG, result.error)
                        }
                        else -> {}
                    }
                }
        } ?: Toast.makeText(
            this@AddStoryActivity,
            resources.getString(R.string.please_insert_an_image),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AddStoryActivity, MainActivity::class.java))
        finish()
    }

    companion object {
        const val CAMERA_RESULT = 200
        private const val TAG = "AddStoryActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}