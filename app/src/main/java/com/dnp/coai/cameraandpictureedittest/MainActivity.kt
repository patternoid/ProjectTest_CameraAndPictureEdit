package com.dnp.coai.cameraandpictureedittest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.dnp.coai.cameraandpictureedittest.camera.CameraMediaStoreActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG : String = "MainActivity"
    val MY_PERMISSION_REQUEST_CODE : Int = 100
    val permissionManager: PermissionManager by lazy {  PermissionManager.instance }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d( TAG, "FileDIr : " + filesDir)
        Log.d( TAG, "External File Dir : " + getExternalFilesDir(Environment.DIRECTORY_PICTURES))

        button_take_photo.setOnClickListener { v ->

            val cameraPermission : String               = android.Manifest.permission.CAMERA
            val gotPermission    : Boolean              = permissionManager.updatePermission(this@MainActivity, cameraPermission)

            if( gotPermission ){
                startCameraActivity()
            }

            else{
                val permissions : Array<String> = arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE )

                permissionManager.requestPermission(this@MainActivity, permissions, MY_PERMISSION_REQUEST_CODE)
            }
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode)
        {
            MY_PERMISSION_REQUEST_CODE ->{

                if( grantResults.isNotEmpty() ){

                    if( permissionManager.isAcceptPermission(grantResults) ){
                        startCameraActivity()
                    }

                    else{
                        val permissions : Array<String> = arrayOf(
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE)

                        permissionManager.requestPermission(this@MainActivity, permissions, MY_PERMISSION_REQUEST_CODE)
                    }
                }
            }
        }
    }




    private fun startCameraActivity(){
        //val intent : Intent = CameraActivity.newIntent(this@MainActivity)
        val intent : Intent = CameraMediaStoreActivity.newIntent(this@MainActivity)
        startActivity(intent)
    }
}
