package com.dnp.coai.cameraandpictureedittest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.net.URI

class CameraMediaStoreActivity : AppCompatActivity() {


    companion object {
        val REQUEST_PHOTO : Int = 2

        fun newIntent( context : Context) : Intent{

            val intent : Intent = Intent( context , CameraMediaStoreActivity::class.java)
            return intent
        }
    }

    private var mPhotoFile : File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_media_store)

        mPhotoFile = getPhotoFile()



        val captureImage : Intent = Intent( MediaStore.ACTION_IMAGE_CAPTURE)

        val canTakePhoto : Boolean = ( mPhotoFile != null && captureImage.resolveActivity(packageManager) != null )

        if( canTakePhoto ){
            var uri : Uri = Uri.fromFile(mPhotoFile)
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        startActivityForResult(captureImage, REQUEST_PHOTO )
    }



    private fun getPhotoFile() : File? {

        var externalFilesDir : File = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        if( externalFilesDir == null )
            return null

        return File( externalFilesDir, "IMG_TEST.jpg")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if( requestCode== REQUEST_PHOTO ){

        }
    }
}
