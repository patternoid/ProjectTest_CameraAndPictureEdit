package com.dnp.coai.cameraandpictureedittest

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by Patternoid on 2017-07-31.
 */

class PermissionManager private constructor(){

    init{

    }

    private object Holder{
        val Instance = PermissionManager()
    }

    companion object {
        val instance : PermissionManager by lazy { Holder.Instance}
        val TAG : String = "PermissionManager"
    }




    fun updatePermission(appContext : Context, permission : String ) : Boolean{

        val APIVersion : Int = android.os.Build.VERSION.SDK_INT

        if( APIVersion >= android.os.Build.VERSION_CODES.M ){
            return checkPermission(appContext, permission)
        }

        else{
            return true
        }
    }



    fun requestPermission( targetActivity : Activity, permission: String, requestCode : Int  ){
        val permissions = arrayOf(permission)
        ActivityCompat.requestPermissions( targetActivity, permissions, requestCode)
    }




    fun retryRequestPermission( targetActivity: Activity , permission : String , requestCode : Int){

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){

            if( targetActivity.shouldShowRequestPermissionRationale( permission) ){

                showMessagePermission("권한 허가를 요청합니다" ,targetActivity,DialogInterface.OnClickListener { dialogInterface, i ->
                    requestPermission(targetActivity, permission, requestCode )

                })
            }
        }
    }



    fun isAcceptPermission( grantResults: IntArray ) : Boolean{
        return (grantResults[0] == PackageManager.PERMISSION_GRANTED)
    }




    private fun checkPermission(appContext : Context, permission : String) : Boolean{

        val result : Int = ContextCompat.checkSelfPermission( appContext, permission)

        return result == PackageManager.PERMISSION_GRANTED
    }



    private fun showMessagePermission( message: String, targetActivity: Activity ,okListener : DialogInterface.OnClickListener ){

        android.support.v7.app.AlertDialog.Builder(targetActivity).apply {
            setMessage(message)
            setPositiveButton("허용", okListener)
            setNegativeButton("거부", null)
            create()
            show()
        }
    }
}