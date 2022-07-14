package com.example.networkmodule.core

import androidx.annotation.Keep
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.net.HttpURLConnection

@Keep
data class ErrorBody(val message: String, val code: Int = -1, val rawMessage: String) {

    fun getValueFromRaw(key: String): String? {
        return try {
            val parser = JsonParser()
            val json: JsonObject = parser.parse(rawMessage).asJsonObject
            json.get(key).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

const val DEFAULT_ERROR_MESSAGE = "Something went wrong. Please try again later."
const val DEFAULT_ERROR_MESSAGE_NO_INTERNET = "Unable to connect with the server. Please check your internet connection"
const val ERROR_CODE_CANCELLATION_JOB = 451
const val ERROR_CODE_TIMEOUT = HttpURLConnection.HTTP_CLIENT_TIMEOUT