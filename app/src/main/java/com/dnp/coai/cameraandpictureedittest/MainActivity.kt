package com.dnp.coai.cameraandpictureedittest

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.Log
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
                permissionManager.requestPermission(this@MainActivity, cameraPermission, MY_PERMISSION_REQUEST_CODE)
            }
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode)
        {
            MY_PERMISSION_REQUEST_CODE ->{

                if(grantResults.size > 0){

                    if( permissionManager.isAcceptPermission(grantResults) ){
                        startCameraActivity()
                    }

                    else{
                        permissionManager.retryRequestPermission( this@MainActivity, android.Manifest.permission.CAMERA, MY_PERMISSION_REQUEST_CODE)
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
