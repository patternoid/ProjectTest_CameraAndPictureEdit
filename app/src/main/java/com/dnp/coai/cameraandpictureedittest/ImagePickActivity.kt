package com.dnp.coai.cameraandpictureedittest

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_image_pick.*
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class ImagePickActivity : AppCompatActivity() {

    companion object {
        val REQUEST_CODE : Int = 1
    }

    private var bitmap      : Bitmap? = null
    private var imageView   : ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pick)
    }


    fun onClick( view : View) {

        val intent : Intent = Intent().apply {
            setType("image/*")
            setAction( Intent.ACTION_GET_CONTENT )
            addCategory( Intent.CATEGORY_OPENABLE )
        }

        startActivityForResult( intent, REQUEST_CODE )
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var stream : InputStream? = null
        if( requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            try{
                stream = contentResolver.openInputStream(data?.data)
                bitmap?.recycle()
                bitmap = BitmapFactory.decodeStream(stream)

                image_view_border.setImageBitmap(bitmap)

            } catch (e : FileNotFoundException){
                e.printStackTrace()
            } finally {
                try {
                    stream?.close()
                }catch (e : IOException){
                    e.printStackTrace()
                }
            }
        }
    }
}
