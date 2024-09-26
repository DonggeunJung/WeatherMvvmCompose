package com.example.weathermvvmcompose.common

import android.content.Context
import android.util.Base64
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow
import kotlin.math.roundToInt

object Util {
    fun roundDigit(number : Double, digits : Int): Double {
        return (number * 10.0.pow(digits.toDouble())).roundToInt() / 10.0.pow(digits.toDouble())
    }

    fun buildRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json {
            isLenient = true
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType())) //Here we are using the GsonConverterFactory to directly convert json data to object
        .build()

    private const val AES_KEY = "aesEncryptionKey"
    private const val INIT_VECTOR = "encryptionIntVec"

    fun decrypt(value: String?): String? {
        try {
            val iv = IvParameterSpec(INIT_VECTOR.toByteArray(charset("UTF-8")))
            val skeySpec = SecretKeySpec(AES_KEY.toByteArray(charset("UTF-8")), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT))
            return String(original)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun hideKeypad(@ApplicationContext context: Context, editText: EditText) {
        val imm: InputMethodManager = context.getSystemService (Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    /*@JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        url?.let {
            Glide.with(view.context).load(url).into(view)
        }
    }*/
}