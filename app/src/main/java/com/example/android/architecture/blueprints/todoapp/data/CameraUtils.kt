package com.example.android.architecture.blueprints.todoapp.data

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.example.android.architecture.blueprints.todoapp.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

fun getCircleBitmap(color: Int, size: Int, activityContext: Context): Drawable {
    val a = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(a)
    val paint = Paint()
    paint.isAntiAlias = true
    paint.color = color
    paint.style = Paint.Style.FILL
    canvas.drawBitmap(a, 0f, 0f, paint)
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    return BitmapDrawable(activityContext.resources, a)
}

 fun saveStreamToFile(inputStream: InputStream,
                             resources: Resources,
                             activity: Context): Pair<String?, String?> {
    var outputPath: String? = null
    var originalOutputPath: String? = null
    try {
        val filesDir = activity.getExternalFilesDir(null)
        val photo: Bitmap?
        val options = BitmapFactory.Options()
        options.inMutable = true
        options.inDensity = 5000
        options.inScreenDensity = 5000
        options.inTargetDensity = 5000

        photo = BitmapFactory.decodeStream(inputStream, null, options)

        inputStream.close()

        val originImageFile = File(filesDir, System.currentTimeMillis().toString() + "_.jpg")
        if (originImageFile.createNewFile()) {
            val fos = FileOutputStream(originImageFile)
            photo!!.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        }
        originalOutputPath = originImageFile.path
        val logo = BitmapFactory.decodeResource(resources, R.drawable.ic_add)

        //----------------------draw timestamp----------------
        val cs = Canvas(photo)
        val tPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        //get timestamp text
        val dateTime = Date().toString()

        //calculating text size
        val textSize = photo.width / 40f
        tPaint.isAntiAlias = true
        tPaint.isFilterBitmap = true
        tPaint.color = Color.WHITE
        tPaint.style = Paint.Style.FILL
        tPaint.textSize = textSize

        val footerHeight = 1.67f * textSize
        //draw background rect
        tPaint.color = Color.BLACK
        tPaint.alpha = 75
        cs.drawRect(0f, photo.height - footerHeight, photo.width.toFloat(), photo.height.toFloat(), tPaint)

        //draw texts
        tPaint.alpha = 100
        tPaint.textSize = textSize
        tPaint.color = Color.WHITE
        tPaint.style = Paint.Style.FILL
        cs.drawText(dateTime, 20f, photo.height - textSize / 2, tPaint)

        val src = Rect(0, 0, logo.width, logo.height)

        val horizontalPadding = 0.2f * footerHeight
        val logoTargetHeight = 0.9f * footerHeight
        val scale = logoTargetHeight / logo.height
        val logoTargetWith = scale * logo.width

        val right = photo.width - horizontalPadding
        val left = right - logoTargetWith
        val bottom = photo.height - (footerHeight - logoTargetHeight) / 2
        val top = bottom - logoTargetHeight

        val dest = RectF(left, top, right, bottom)
        val logoPaint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        cs.drawBitmap(logo, src, dest, logoPaint)
        val text = "TODO application"
        val textWith = tPaint.measureText(text)
        cs.drawText(text, dest.left - horizontalPadding - textWith, photo.height - textSize / 2, tPaint)

        //-----------------compress bitmap------------------
        val bytes = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        bytes.close()
        photo.recycle()

        //--------------------save to file----------------------
        val filePath = File(filesDir, System.currentTimeMillis().toString() + ".jpg").toString()
        val f = File(filePath)
        if (f.createNewFile()) {
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            fo.close()
        }
        outputPath = filePath

    } catch (ignored: Throwable) {
    }
    return Pair(originalOutputPath, outputPath)
}

suspend fun drawOnPhotoFile(photoFromCamera: File,
                    resources: Resources,
                    activity: Context): Pair<String?, String?> {
    var outputPath: String? = null
    var originalOutputPath: String? = null
    try {
        originalOutputPath = photoFromCamera.path
        val options = BitmapFactory.Options()
        options.inMutable = true
        val photo: Bitmap? = BitmapFactory.decodeFile(originalOutputPath, options)
        outputPath = photo?.let { drawLogoOnPhotoFile(it, resources, activity) }
    } catch (ignored: Throwable) {
    }
    return Pair(originalOutputPath, outputPath)
}

private fun drawLogoOnPhotoFile(photo: Bitmap, resources: Resources,
                                activity: Context): String {
    val logo = BitmapFactory.decodeResource(resources, R.drawable.ic_add)
    val filesDir = activity.getExternalFilesDir(null)

    //----------------------draw timestamp----------------
    val cs = Canvas(photo)
    val tPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //get timestamp text
    val dateTime = Date().toString()


    //calculating text size
    val textSize = photo.width / 40f
    tPaint.isAntiAlias = true
    tPaint.isFilterBitmap = true
    tPaint.color = Color.WHITE
    tPaint.style = Paint.Style.FILL
    tPaint.textSize = textSize

    val footerHeight = 1.67f * textSize
    //draw background rect
    tPaint.color = Color.BLACK
    tPaint.alpha = 75
    cs.drawRect(0f, photo.height - footerHeight, photo.width.toFloat(), photo.height.toFloat(), tPaint)

    //draw texts
    tPaint.alpha = 100
    tPaint.textSize = textSize
    tPaint.color = Color.WHITE
    tPaint.style = Paint.Style.FILL
    cs.drawText(dateTime, 20f, photo.height - textSize / 2, tPaint)

    val src = Rect(0, 0, logo.width, logo.height)

    val horizontalPadding = 0.2f * footerHeight
    val logoTargetHeight = 0.9f * footerHeight
    val scale = logoTargetHeight / logo.height
    val logoTargetWith = scale * logo.width

    val right = photo.width - horizontalPadding
    val left = right - logoTargetWith
    val bottom = photo.height - (footerHeight - logoTargetHeight) / 2
    val top = bottom - logoTargetHeight

    val dest = RectF(left, top, right, bottom)
    val logoPaint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    cs.drawBitmap(logo, src, dest, logoPaint)
    val text = "TODO application"
    val textWith = tPaint.measureText(text)
    cs.drawText(text, dest.left - horizontalPadding - textWith, photo.height - textSize / 2, tPaint)

    //-----------------compress bitmap------------------
    val bytes = ByteArrayOutputStream()
    photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    bytes.close()
    photo.recycle()

    //--------------------save to file----------------------
    val filePath = File(filesDir, System.currentTimeMillis().toString() + ".jpg").toString()
    val f = File(filePath)
    if (f.createNewFile()) {
        val fo = FileOutputStream(f)
        fo.write(bytes.toByteArray())
        fo.close()
    }
    return filePath
}



