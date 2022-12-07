package com.example.storyappsubmission1.Activity.Main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission1.Data.Functon.CompressImage
import com.example.storyappsubmission1.databinding.ActivityStoryBinding
import com.example.storyappsubmission1.Data.API.Config
import com.example.storyappsubmission1.Data.Functon.Bitmap
import com.example.storyappsubmission1.Data.Functon.uriToFile
import com.example.storyappsubmission1.Data.Functon.Preference
import com.example.storyappsubmission1.Data.Response.GeneralR
import com.example.storyappsubmission1.Data.Response.LoginResult
import com.example.storyappsubmission1.ViewModel.FactoryVM
import com.example.storyappsubmission1.ViewModel.StoryVM
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyViewModel: StoryVM
    private var imageFile: File? = null

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
                    "Gagal Mendapatkan Permsission",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val pref = Preference.getInstance(dataStore)
        storyViewModel =
            ViewModelProvider(this, FactoryVM(pref))[StoryVM::class.java]

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnTakepciture.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { addStory() }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Failed to chose Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun addStory() {
        if (imageFile != null) {
            val file = CompressImage(imageFile as File)
            val description =
                binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            storyViewModel.getUser().observe(
                this
            ) { user: LoginResult ->
                val service = Config.getApiService()
                    .addStories("Bearer ${user.token}", imageMultipart, description, "0".toFloat(), "0".toFloat())
                showLoading(true)
                service.enqueue(object : Callback<GeneralR> {
                    override fun onResponse(
                        call: Call<GeneralR>,
                        response: Response<GeneralR>
                    ) {
                        if (response.isSuccessful) {
                            showLoading(false)
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                Toast.makeText(
                                    this@StoryActivity,
                                    responseBody.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                intentMain()
                            }
                        } else {
                            showLoading(false)
                            Toast.makeText(
                                this@StoryActivity,
                                response.message(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<GeneralR>, t: Throwable) {
                        showLoading(false)
                        Toast.makeText(
                            this@StoryActivity,
                            "Gagal Melakukan Posting",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        } else {
            Toast.makeText(
                this@StoryActivity,
                "Failed To Upload",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            imageFile = myFile

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = Bitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.ivStoryPref.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@StoryActivity)
            imageFile = myFile
            binding.ivStoryPref.setImageURI(selectedImg)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun intentMain() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        showLoading(false)
        finish()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}