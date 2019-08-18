package com.nixo.colagauss

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.IntRange
import android.support.annotation.NonNull
import android.view.View
import android.widget.ImageView
import java.lang.Exception

class GaussBuilder (val context: Context){

    private var  degree = 0
    private var  url = ""
    // 1->网络高斯模糊
    // 2->本地图片
    private var tag = 1
    val regex : Regex = Regex("preg = \"/^http(s)?:\\\\/\\\\/.+/\"")
    private var  bitmap: Bitmap? = null
    var renderScriptBitmapBlur = RenderScriptBitmapBlur(context)



    fun  degree(@IntRange(from = 1, to = 25) scala: Int ):GaussBuilder{
        this.degree = scala
        return this
    }

    fun url(@NonNull url:String):GaussBuilder{
        var find = regex.find(url)
        if(find != null){
            this.url = url
        }else{
            throw Exception("This is Not be URL.")
        }

        return this
    }

    fun resources(path : Int):GaussBuilder{
        bitmap = BitmapFactory.decodeResource(context.resources, path)
        return this
    }

    suspend fun asBitmap()= when(tag){
        1->renderScriptBitmapBlur.getBlurBitmapForUrl(degree,url)
        2->renderScriptBitmapBlur.getBlurBitmap(degree,bitmap!!)
        else -> null
    }

    suspend fun load(view:View){
        view.background = when(tag){
            1-> BitmapDrawable(renderScriptBitmapBlur.getBlurBitmapForUrl(degree,url))
            2->BitmapDrawable(renderScriptBitmapBlur.getBlurBitmap(degree,bitmap!!))
            else -> null
        }
    }

    suspend fun load(view:ImageView){
        view.setImageBitmap(when(tag){
            1-> renderScriptBitmapBlur.getBlurBitmapForUrl(degree,url)
            2-> renderScriptBitmapBlur.getBlurBitmap(degree,bitmap!!)
            else -> null
        })
    }

}