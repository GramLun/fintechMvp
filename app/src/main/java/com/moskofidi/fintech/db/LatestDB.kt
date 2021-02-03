package com.moskofidi.fintech.db

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import com.bumptech.glide.load.resource.gif.GifDrawable
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

class LatestDB(context: Context) {
    fun saveToInternalStorage(context: Context, name: String, bitmap: Bitmap) {
        val cw = ContextWrapper(context)
        val dir = cw.getDir("'latestGif", Context.MODE_PRIVATE)
        val path = File(dir, name)

        val fos: FileOutputStream
        try {
            fos = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat, 100, fos);
        } catch (e: Exception) {

        } finally {
            fos.close()
        }
    }

    fun f(context: Context) {
        val newGifDrawable = ((imgViewLatest.drawable).constantState.newDrawable().mutate()) as GifDrawable
        val byteBuffer = (imgViewLatest.drawable as GifDrawable).buffer
        val cw = ContextWrapper(context)
        val dir = cw.getDir("'latestGif", Context.MODE_PRIVATE)
        val path = File(dir, "test.gif")

        val output = FileOutputStream(path)
        val bytes = ByteArray(byteBuffer.capacity())
        (byteBuffer.duplicate().clear() as ByteBuffer).get(bytes)
        output.write(bytes, 0 ,bytes.size)
        output.close()
    }
}