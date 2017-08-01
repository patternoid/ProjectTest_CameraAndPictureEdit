package com.dnp.coai.cameraandpictureedittest.GridViewTest

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

import com.dnp.coai.cameraandpictureedittest.R
import kotlinx.android.synthetic.main.activity_grid_view_test.*

class GridViewTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_view_test)


        val img : IntArray = kotlin.IntArray(200)

        for( i in 0..199 )
            img[i] = R.drawable.temp


        var adapter : MyAdapter = MyAdapter( applicationContext, R.layout.row, img )

        grid_view_holder.adapter = adapter

        grid_view_holder.setOnItemClickListener { adapterView, view, i, l ->

        }

        grid_view_holder.setOnTouchListener { view, motionEvent ->

            super.onTouchEvent(motionEvent)

            if( motionEvent!!.action == MotionEvent.ACTION_DOWN){

                val x : Float = motionEvent.getX()
                val y : Float = motionEvent.getY()

                var msg : String = "터치입력을 받음 - x : "  + x + " y : " + y


                Log.d("MyView" , msg )

                return@setOnTouchListener true
            }


            Log.d( "TAG ", "MEASURE WIDTH : " + grid_view_holder.measuredWidth )
            Log.d( "TAG ", "MEASURE HEIGHT : " + grid_view_holder.measuredHeight )

            
            return@setOnTouchListener false
        }



    }
 }







class MyAdapter constructor( c : Context, l : Int, i : IntArray ): BaseAdapter(){

    var context : Context? = null
    var layout  : Int = 0
    var img     : IntArray? = null
    var inf     : LayoutInflater? = null


    init {
        context =  c
        layout  =  l
        img     =  i

        inf     = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }


    override fun getCount(): Int {
        return img!!.size
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getItem(p0: Int): Any {
        return img!![p0]
    }

    override fun getView(position : Int, convertView : View?, parent : ViewGroup?): View {

        var view: View? = null
        if (convertView == null)
            view = inf!!.inflate(layout, null)
        else
            view = convertView

        var imageViewChunk = view!!.findViewById<ImageView>(R.id.image_view_chunk)
        imageViewChunk.setImageResource(img!![position])

        return view!!
    }
}