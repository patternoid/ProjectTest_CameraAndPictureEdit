package com.dnp.coai.cameraandpictureedittest.Touch

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View

import com.dnp.coai.cameraandpictureedittest.R

class TouchTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var view : View = MyView(this)
        setContentView(view)
    }




    protected class MyView : View{

        constructor(context : Context) : super(context)


        override fun onTouchEvent(event: MotionEvent?): Boolean {

            super.onTouchEvent(event)

            if( event!!.action == MotionEvent.ACTION_DOWN){

                val x : Float = event.getX()
                val y : Float = event.getY()

                var msg : String = "터치입력을 받음 - x : "  + x + " y : " + y


                Log.d("MyView" , msg )

                return true
            }


            return false
        }
    }
}
