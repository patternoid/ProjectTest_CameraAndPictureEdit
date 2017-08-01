package com.dnp.coai.cameraandpictureedittest.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.media.ExifInterface
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.dnp.coai.cameraandpictureedittest.R
import kotlinx.android.synthetic.main.activity_camera_media_store.*
import java.io.File


class CameraMediaStoreActivity : AppCompatActivity() {


    companion object {
        private val TAG : String = "CameraMediaStore"
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

      /*  val intent      : Intent = Intent()
        val camera      : Camera = Camera.open()
        val parameters  : Camera.Parameters = camera.parameters

        var sizeList    : List<Camera.Size?> = parameters.supportedPictureSizes

        Log.d( TAG, "--SupportedPictureSizeList Start--")

        for( size : Camera.Size? in sizeList ){
            Log.d( TAG, "Width : " + size!!.width + ", Height : " + size!!.height )
        }
        Log.d( TAG, "--SupportedPictureSizeList End--")


        val size : Camera.Size = getOptimalPictureSize( parameters.supportedPictureSizes, 1280, 720)
        Log.d( TAG, "Selected Optiaml Size : (" + size.width + ", " + size.height + ")")

        parameters.setPreviewSize(size.width, size.height)
        parameters.setPictureSize(size.width, size.height)

        camera.parameters = parameters
        camera.release()*/

















        mPhotoFile = getPhotoFile()
        val captureImage : Intent = Intent( MediaStore.ACTION_IMAGE_CAPTURE)

        //아래 resolveActivity는 현재 ACTION_IMAGE_CAPTURE를 실행할 수 있는 외부 어플리케이션이 존재하는지
        //확인한다. 만약 null을 반환한다면 처리할 수 있는 어플리케이션이 없다는 말이다.
        val canTakePhoto : Boolean = ( mPhotoFile != null && captureImage.resolveActivity(packageManager) != null )

        if( canTakePhoto ){
            var uri : Uri = Uri.fromFile(mPhotoFile)
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        startActivityForResult(captureImage, REQUEST_PHOTO)
    }




    //지정한 해상도에 가장 최적화 된 카메라 캡쳐 사이즈 구해주는 함수
    private fun getOptimalPictureSize(sizeList: List<Camera.Size>, width: Int, height: Int): Camera.Size {
        Log.d(TAG, "getOptimalPictureSize, 기준 width,height : ($width, $height)")

        var prevSize = sizeList[0]
        var optSize = sizeList[1]

        for (size in sizeList) {
            // 현재 사이즈와 원하는 사이즈의 차이
            val diffWidth       = Math.abs(size.width - width)
            val diffHeight      = Math.abs(size.height - height)

            // 이전 사이즈와 원하는 사이즈의 차이
            val diffWidthPrev   = Math.abs(prevSize.width - width)
            val diffHeightPrev  = Math.abs(prevSize.height - height)

            // 현재까지 최적화 사이즈와 원하는 사이즈의 차이
            val diffWidthOpt    = Math.abs(optSize.width - width)
            val diffHeightOpt   = Math.abs(optSize.height - height)


            // 이전 사이즈보다 현재 사이즈의 가로사이즈 차이가 적을 경우 && 현재까지 최적화 된 세로높이 차이보다 현재 세로높이 차이가 적거나 같을 경우에만 적용
            if (diffWidth < diffWidthPrev && diffHeight <= diffHeightOpt) {
                optSize = size
                Log.d(TAG, "가로사이즈 변경 / 기존 가로사이즈 : " + prevSize.width + ", 새 가로사이즈 : " + optSize.width)
            }

            // 이전 사이즈보다 현재 사이즈의 세로사이즈 차이가 적을 경우 && 현재까지 최적화 된 가로길이 차이보다 현재 가로길이 차이가 적거나 같을 경우에만 적용
            if (diffHeight < diffHeightPrev && diffWidth <= diffWidthOpt) {
                optSize = size
                Log.d(TAG, "세로사이즈 변경 / 기존 세로사이즈 : " + prevSize.height + ", 새 세로사이즈 : " + optSize.height)
            }

            // 현재까지 사용한 사이즈를 이전 사이즈로 지정
            prevSize = size
        }

        Log.d(TAG, "결과 OptimalPictureSize : " + optSize.width + ", " + optSize.height)
        return optSize
    }




    private fun getPhotoFile() : File? {

        var externalFilesDir : File = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        if( externalFilesDir == null )
            return null

        return File( externalFilesDir, "IMG_TEST.jpg")
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if( resultCode != Activity.RESULT_OK)
            return

        if( requestCode== REQUEST_PHOTO){
/*
            val options : BitmapFactory.Options = BitmapFactory.Options()

            options.inPreferredConfig = Bitmap.Config.RGB_565
            val bitmap : Bitmap = BitmapFactory.decodeFile( mPhotoFile!!.path , options)

            //이미지 뷰에 비트맵을 올린다.
            image_view_photo_holder.setImageBitmap(bitmap)
*/

            try{
                val options : BitmapFactory.Options = BitmapFactory.Options()

                options.inPreferredConfig = Bitmap.Config.RGB_565
                var bitmap : Bitmap = BitmapFactory.decodeFile( mPhotoFile!!.path , options)

                //이미지를 상황에 맞게 회전시킨다.

                val exif : ExifInterface = ExifInterface( mPhotoFile!!.path )
                val exifOrientation : Int = exif.getAttributeInt( ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL )
                val exifDegree : Int = exifOrientationToDegrees( exifOrientation )

                bitmap = rotate( bitmap, exifDegree )

                //이미지 뷰에 비트맵을 올린다.
                image_view_photo_holder.setImageBitmap(bitmap)
            }

            catch( e : Exception ){
                Toast.makeText( this@CameraMediaStoreActivity, "오류 발생: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }




    //EXIF 정보를 회전각도로 변환하는 메서드
    //@param exifOrientation : EXIF 회전각
    //@param return : 실제 각도
    fun exifOrientationToDegrees( exifOrientation : Int ) : Int{

        when( exifOrientation ){
            ExifInterface.ORIENTATION_ROTATE_90 -> return 90
            ExifInterface.ORIENTATION_ROTATE_180 -> return 180
            ExifInterface.ORIENTATION_ROTATE_270 -> return 270

            else -> return 0
        }
    }



    //이미지를 회전 시킨다.
    //@param bitmap : 비트맵 이미지
    //@param degress : 회전 각도
    //return 회전된 이미지

    fun rotate( bitmap : Bitmap , degrees : Int) : Bitmap{

        if( degrees != 0 && bitmap != null ){
            val matrix : Matrix = Matrix()




            matrix.setRotate(degrees.toFloat(), (bitmap.width /2).toFloat(), (bitmap.height /2).toFloat())

            try{
                var converted : Bitmap = Bitmap.createBitmap( bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                if( bitmap != converted ){
                    bitmap.recycle()
                    //bitmap = converted
                    return converted
                }
            }
            catch( e : OutOfMemoryError)
            {

            }
        }
        return bitmap
    }
}
