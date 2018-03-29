package com.changjiangev.cjev.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider

import com.sihaiwanlian.baselib.base.BaseActivity
import com.sihaiwanlian.baselib.utils.CommonUtil
import com.sihaiwanlian.baselib.utils.CommonUtil.hasSdcard
import com.sihaiwanlian.baselib.utils.PhotoUtils
import com.sihaiwanlian.baselib.utils.ToastUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by zhu on 2017/8/14.
 */

abstract class BasePhotoActivity : BaseActivity() {
    private val fileUri = File(Environment.getExternalStorageDirectory().path + "/" + SystemClock.currentThreadTimeMillis() + ".jpg")
    private val fileCropUri = File(Environment.getExternalStorageDirectory().path + "/" + SystemClock.currentThreadTimeMillis() + ".jpg")
    private val fileHand = File(Environment.getExternalStorageDirectory().path + "/" + SystemClock.currentThreadTimeMillis() + "1.jpg")
    private val fileInfront = File(Environment.getExternalStorageDirectory().path + "/" + SystemClock.currentThreadTimeMillis() + "2.jpg")
    private val fileBack = File(Environment.getExternalStorageDirectory().path + "/" + SystemClock.currentThreadTimeMillis() + "3.jpg")

    private var imageUri: Uri? = null
    private val cropImageUri: Uri? = null
    private var type: Int = 0


    abstract val childView: Int

    override fun getContentView(): Int {
        return childView
    }

    override fun initView() {

    }

    override fun initData() {

    }

    fun autoObtainCameraPermission(type: Int) {
        this.type = type
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                //                ToastUtils.showToastCenter("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), CAMERA_PERMISSIONS_REQUEST_CODE)
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(mContext, mContext.packageName + ".fileprovider", fileUri)//通过FileProvider创建一个content类型的Uri

                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST)
            } else {
                ToastUtils.showToastCenter("设备没有SD卡！")
            }
        }
    }

    fun autoObtainStoragePermission(type: Int) {
        this.type = type
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSIONS_REQUEST_CODE)
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSIONS_REQUEST_CODE -> {//调用系统相机申请拍照权限回调
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (CommonUtil.hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = FileProvider.getUriForFile(mContext, mContext.packageName + ".fileprovider", fileUri)//通过FileProvider创建一个content类型的Uri
                        }
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST)
                    } else {
                        ToastUtils.showToastCenter("设备没有SD卡！")
                    }
                } else {
                    ToastUtils.showToastCenter("请允许打开相机！！")
                }


            }
            STORAGE_PERMISSIONS_REQUEST_CODE//调用系统相册申请Sdcard权限回调
            -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PhotoUtils.openPic(this, CODE_GALLERY_REQUEST)
            } else {
                ToastUtils.showToastCenter("您拒绝了权限，无法使用相机功能")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_CAMERA_REQUEST) {
                //                cropImageUri = Uri.fromFile(fileCropUri);
                //                PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                val bitmapFromUri = PhotoUtils.getBitmapFromUri(imageUri, this)
                val bitmapDegree = getBitmapDegree(imageUri!!.path)
                val bitmap = rotateBitmapByDegree(bitmapFromUri, bitmapDegree)
                if (bitmap == null) {
                    showToastBottom("您拍摄的照片不符合要求!")
                    return
                }
                try {
                    val fileIcon = File(Environment.getExternalStorageDirectory().path + "/" + SystemClock.currentThreadTimeMillis() + "4.jpg")
                    saveBitmapToUri(bitmap, fileIcon.absolutePath)
                    getbitmap(type, fileIcon.path, bitmap)
                } catch (e: IOException) {
                    showToastBottom("图片压缩异常")
                }

            } else if (CODE_GALLERY_REQUEST == requestCode) {
                if (hasSdcard()) {
                    //                    cropImageUri = Uri.fromFile(fileCropUri);
                    var newUri = Uri.parse(PhotoUtils.getPath(this, data.data))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(this, mContext.packageName + ".fileprovider", File(newUri.path))
                    }
                    val bitmapDegree = getBitmapDegree(newUri.path)

                    val bitmapFromUri = PhotoUtils.getBitmapFromUri(newUri, this)
                    if (bitmapFromUri == null) {
                        showToastBottom("您选择的图片不正确!")
                        return
                    }
                    val bitmap = rotateBitmapByDegree(bitmapFromUri, bitmapDegree)
                    if (bitmap == null) {
                        showToastBottom("您选择的图片不正确!")
                        return
                    }
                    try {
                        val fileIcon = File(Environment.getExternalStorageDirectory().path + "/" + SystemClock.currentThreadTimeMillis() + "4.jpg")
                        saveBitmapToUri(bitmap, fileIcon.absolutePath)
                        getbitmap(type, fileIcon.path, bitmap)
                    } catch (e: IOException) {
                        showToastBottom("图片压缩异常")
                    }

                    //                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);

                } else {
                    ToastUtils.showToastCenter("您的设备没有SD卡！")
                }
                //            } else if (CODE_RESULT_REQUEST == requestCode) {
                //                Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                //                if (bitmap != null && cropImageUri != null) {
                //
                ////                    getbitmap(type, cropImageUri, bitmap);
                //                }
            }
        }
    }

    protected abstract fun getbitmap(type: Int, imagePath: String, bitmap: Bitmap)

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private fun getBitmapDegree(path: String): Int {
        var degree = 0
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            val exifInterface = ExifInterface(path)
            // 获取图片的旋转信息
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    fun rotateBitmapByDegree(bm: Bitmap?, degree: Int): Bitmap? {
        var returnBm: Bitmap? = null
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm!!, 0, 0, bm.width, bm.height, matrix, true)
        } catch (e: OutOfMemoryError) {

        }

        if (returnBm == null) {
            returnBm = bm
        }
        if (bm != returnBm) {
            bm!!.recycle()
        }
        return returnBm
    }

    /**
     * 把压缩后的bitmap转化为file;
     *
     * @param path;压缩后的file路径
     */
    @Throws(IOException::class)
    private fun saveBitmapToUri(bitmap: Bitmap, path: String): Boolean {
        val file = File(path)
        if (file.exists()) {
            if (file.delete()) {
                if (!file.createNewFile()) {
                    return false
                }
            }
        }
        val outStream = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)//100为压缩的品质,100为100%
        outStream.flush()
        outStream.close()
        return true
    }

    companion object {
        private val CAMERA_PERMISSIONS_REQUEST_CODE = 111
        private val STORAGE_PERMISSIONS_REQUEST_CODE = 222
        //    private static final int CODE_RESULT_REQUEST = 333;
        val CODE_CAMERA_REQUEST = 200
        val CODE_GALLERY_REQUEST = 300


        private val output_X = 480
        private val output_Y = 480
    }
}
