package com.example.storyapp.ui

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.*

import com.example.storyapp.databinding.ActivityStoryBinding
import com.example.storyapp.ui.viewModel.StoryUploadActivityViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Suppress("DEPRECATION")
class StoryActivity : AppCompatActivity() {
    private lateinit var storyBinding: ActivityStoryBinding
    private var getFile : File?=null
    private lateinit var storyUploadActivityViewModel : StoryUploadActivityViewModel

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val authorityEntry = "com.example.storyapp"
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
                    "Tidak Mendapatkan permission.",
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
        storyBinding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(storyBinding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupViewModel()

        storyBinding.btnCameraOption.setOnClickListener { startCameraX() }
        storyBinding.btnGalleryOption.setOnClickListener { startGallery() }
        storyBinding.btnUpload.setOnClickListener { uploadImage() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun setupViewModel(){
        storyUploadActivityViewModel = ViewModelProvider(this)[StoryUploadActivityViewModel::class.java]

    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startIntentCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@StoryActivity,
                authorityEntry,
                it
                )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent,"Choose a Picture")
        launcherGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (getFile != null){
            val file = reduceFileImage(getFile as File)

            val description = storyBinding.inputDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultiPart : MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            storyUploadActivityViewModel.apply {
                uploadStory(this@StoryActivity,imageMultiPart,description)

                isLoading.observe(this@StoryActivity){
                    showLoading(it)
                }
                isSuccess.observe(this@StoryActivity){

                    if (it) showDialogs(resources.getString(R.string.title_success_upload_image),resources.getString(R.string.message_success_upload_image),resources.getString(R.string.instruction_success),it)
                    else showDialogs(resources.getString(R.string.title_failed_upload_image),resources.getString(R.string.message_failed_upload_image),resources.getString(R.string.instruction_failed),it)
                }
            }

        }else{
            Toast.makeText(this@StoryActivity,resources.getString(R.string.instruction_empty_upload_image),Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                storyBinding.tvImageUpload.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }


    private lateinit var currentPhotoPath : String
    private val  launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            val resultFix = BitmapFactory.decodeFile(myFile.path)
            myFile.let {file ->
                getFile = file
                storyBinding.tvImageUpload.setImageBitmap(resultFix)
            }


        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val selectedImage = it.data?.data as Uri
            selectedImage.let { uri->
                val myFile = uriToFile(uri,this@StoryActivity)
                getFile = myFile
                storyBinding.tvImageUpload.setImageURI(uri)
            }
        }
    }



    private fun showLoading(state: Boolean) {
        storyBinding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }



    private fun showDialogs(title: String, message: String, instruction: String, state: Boolean) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(instruction) { _, _ ->
                if (state) {
                    val intent = Intent(context, MainActivityStoryApp::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
            }
            create()
            show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}