package com.dnp.coai.cameraandpictureedittest.camera

import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.dnp.coai.cameraandpictureedittest.PermissionManager
import com.dnp.coai.cameraandpictureedittest.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() , Camera.PictureCallback {

    companion object {
        val TAG : String = "CameraActivity"
        val MEDIA_TYPE_IMAGE : Int = 1
        val MEDIA_TYPE_VIDEO : Int = 2

        fun newIntent( context : Context) : Intent {
            val intent : Intent = Intent( context,  CameraActivity::class.java )
            return intent
        }
    }



    private var mCamera : Camera? = null
    private var mPreview: CameraPreview? = null
    val permissionManager: PermissionManager by lazy { PermissionManager.instance }
    val MY_PERMISSION_REQUEST_CODE : Int = 100



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        mCamera = getCameraInstance()

        mCamera?.let{
            mPreview = CameraPreview(this@CameraActivity, mCamera!!)
            camera_preview.addView(mPreview)

            button_capture.setOnClickListener { v ->
                /*
                val permission      : String               = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                val gotPermission   : Boolean              = permissionManager.updatePermission(this@CameraActivity, permission)

                if( gotPermission ){
                    mCamera!!.takePicture(null, null, this@CameraActivity )
                }

                else{
                    permissionManager.requestPermission(this@CameraActivity, permission, MY_PERMISSION_REQUEST_CODE)
                }
                */
                mCamera!!.takePicture(null, null, this@CameraActivity )
            }
        }
    }



/*

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode)
        {
            MY_PERMISSION_REQUEST_CODE ->{

                if( grantResults.isNotEmpty() ){

                    if( permissionManager.isAcceptPermission(grantResults) ){
                        mCamera!!.takePicture(null, null, this@CameraActivity )
                    }

                    else{
                        permissionManager.retryRequestPermission( this@CameraActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSION_REQUEST_CODE)
                    }
                }
            }
        }
    }

*/




    private fun getCameraInstance() : Camera?{
        var camera : Camera? = null

        try{
            camera = Camera.open(0)
        }catch ( e : Exception ){
            //Camera is no available
            e.printStackTrace()
        }

        return camera
    }




    override fun onPictureTaken(data : ByteArray?, camera : Camera?) {

        var pictureFile : File? = getOutputMediaFile(MEDIA_TYPE_IMAGE)

        pictureFile?.let{

            try{

                val fos : FileOutputStream = FileOutputStream(pictureFile)
                fos.write(data)
                fos.close()

                mCamera!!.startPreview()

            } catch ( e : Exception ){
                when(e)
                {
                    is FileNotFoundException ->
                        Log.d(TAG, "File not found : " + e.message )

                    is IOException ->
                        Log.d(TAG, "Error accessing file : " + e.message )

                    else -> throw e
                }
            }
        }
    }



    fun getOutputMediaFile( type : Int ) : File?{

        val mediaStorageDir = File( Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) , "MyCameraApp" )

        if( !mediaStorageDir.exists() ){
            if( !mediaStorageDir.mkdirs() ){
                Log.d("MyCameraApp", "failed to create directory")
                return null
            }
        }

        var timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format( Date() )
        var mediaFile : File?  = null

        when(type)
        {
            MEDIA_TYPE_IMAGE -> {
                mediaFile = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
            }

            MEDIA_TYPE_VIDEO -> {
                mediaFile = File(mediaStorageDir.path + File.separator + "VID_" + timeStamp + ".mp4")
            }
        }

        return mediaFile
    }



    override fun onPause() {
        super.onPause()
        releaseCamera()
    }



    private fun releaseCamera(){
        mCamera?.release()
        mCamera = null
    }
}
