package com.example.weathermvvmcompose.common

import android.app.Activity.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Base64
import com.example.weathermvvmcompose.presentation.App
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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

    fun buildSP(): SharedPreferences = App.context.getSharedPreferences("setup", MODE_PRIVATE)

    fun savePrefCity(state: String, city: String) {
        val pref: SharedPreferences = buildSP()
        val editor = pref.edit()
        editor.putString(KEY_STATE, state)
        editor.putString(KEY_CITY, city)
        editor.apply()
    }

    fun readPrefCity(): Pair<String,String> {
        val pref: SharedPreferences = buildSP()
        val state = pref.getString(KEY_STATE, "") ?: ""
        val city = pref.getString(KEY_CITY, "") ?: ""
        return Pair(state, city)
    }

    private const val AES_KEY = "aesEncryptionKey"
    private const val INIT_VECTOR = "encryptionIntVec"
    const val KEY_STATE = "state"
    const val KEY_CITY = "city"
}