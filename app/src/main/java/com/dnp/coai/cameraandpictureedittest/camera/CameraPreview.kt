package com.dnp.coai.cameraandpictureedittest.camera

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

/**
 * Created by Patternoid on 2017-07-31.
 */

class CameraPreview: SurfaceView , SurfaceHolder.Callback{

    companion object {
        val TAG : String = "CameraPreview"
    }

    private var mHolder : SurfaceHolder? = null
    private var mCamera : Camera? = null



    constructor(context : Context, camera : Camera) : super(context){
        mCamera = camera
        mHolder = holder.apply {
            addCallback(this@CameraPreview)
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }

        Log.d(TAG, "Contruct Called ")
    }



    override fun surfaceCreated( holder : SurfaceHolder){

        Log.d(TAG, "surfaceCreated Called ")

        //서피스가 생성 되었으므로 미리보기를 그릴 위치를 카메라에 알려준다.
        try {
            mCamera!!.setPreviewDisplay(holder)
            mCamera!!.startPreview()

        } catch (e : IOException){
            Log.d(TAG, "Error setting camera preview: " + e.message )
        }

    }



    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

        Log.d(TAG, "surfaceChanged Called ")


        if( mHolder!!.surface == null )
            return

        try {
            mCamera!!.stopPreview()
        } catch ( e : Exception ){
            e.printStackTrace()
        }

        try{
            mCamera!!.setPreviewDisplay(mHolder)
            mCamera!!.startPreview()
        } catch ( e : Exception){
            Log.d(TAG, "Error starting caemra preview: " + e.message)
        }

    }




    override fun surfaceDestroyed(p0: SurfaceHolder?) {

        Log.d(TAG, "surfaceDestroyed Called ")
    }
}


