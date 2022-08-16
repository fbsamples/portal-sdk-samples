// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.portal.sdk.app.fbns

import android.content.SharedPreferences
import android.text.TextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future

import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

/** Emulates what steps the server would take to send a message to the client */
class FbnsMsgSender(private val sp: SharedPreferences) {

  fun sendNotificationAsync(payload: String, tokenId: String) =
    GlobalScope.future {
      sendNotification(payload, tokenId)
    }

  @Throws(GraphException::class)
  suspend fun sendNotification(payload: String, tokenId: String) =
      withContext(Dispatchers.Main) {
        val prevToken = sp.getString(FBNSConstants.PREF_SENDER_ACCESS_TOKEN, null)
        val accessToken =
            if (prevToken != null) {
              prevToken
            } else {
              val tmpToken = genAccessToken()
              sp.edit().putString(FBNSConstants.PREF_SENDER_ACCESS_TOKEN, tmpToken).apply()
              tmpToken
            }

        genSendNotification(payload, tokenId, accessToken)
      }

  private suspend fun genSendNotification(payload: String, tokenId: String, accessToken: String) =
      withContext(Dispatchers.IO) {
        val connection = openHttpsConnection(GRAPH_URL_SEND_MSG_BASE_FORMAT, "POST")
        val params =
            ArrayList<String>().apply {
              addKeyValueString(FIELD_PAYLOAD, payload)
              addKeyValueString(FIELD_TOKEN_ID, tokenId)
              addKeyValueString(FIELD_ACCESS_TOKEN, accessToken)
            }
        connection.writeParamsToConnection(params)

        if (connection.responseCode >= 400) {
          throw GraphException(connection.getErrorTrace())
        }
        connection.disconnect()
      }

  @Throws(GraphException::class)
  private suspend fun genAccessToken(): String =
      withContext(Dispatchers.IO) {
        val connection = openHttpsConnection(GRAPH_URL_CREATE_ACCESS_TOKEN_BASE_FORMAT, "GET")
        val params =
            ArrayList<String>().apply {
              addKeyValueString(FIELD_CLIENT_ID, FBNSConstants.APP_ID)
              addKeyValueString(FIELD_CLIENT_SECRET, FBNSConstants.APP_SECRET)
              addKeyValueString("grant_type", "client_credentials")
            }
        connection.writeParamsToConnection(params)

        if (connection.responseCode >= 400) {
          throw GraphException(connection.getErrorTrace())
        }
        val json = JSONObject(stringifyStream(connection.inputStream))
        connection.disconnect()
        return@withContext json.getString(JSON_KEY_ACCESS_TOKEN)
      }

  @Throws(IOException::class)
  private fun openHttpsConnection(urlString: String, method: String): HttpsURLConnection {
    val connection = URL(urlString).openConnection() as HttpsURLConnection
    connection.requestMethod = "POST"
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    connection.doOutput = true
    return connection
  }

  @Throws(IOException::class)
  private fun HttpsURLConnection.writeParamsToConnection(params: List<String>) =
      DataOutputStream(outputStream).use {
        it.writeBytes(TextUtils.join(FIELD_DELIMITER, params))
        it.flush()
      }

  @Throws(JSONException::class)
  private fun HttpsURLConnection.getErrorTrace(): String {
    val json = JSONObject(stringifyStream(errorStream))
    return json.getJSONObject(JSON_KEY_ERROR).getString(JSON_KEY_FBTRACE_ID)
  }

  @Throws(IOException::class)
  private fun MutableList<String>.addKeyValueString(key: String, value: String) =
      add("%s=%s".format(key, URLEncoder.encode(value, "UTF-8")))

  @Throws(IOException::class)
  private fun stringifyStream(input: InputStream): String {
    val result = ByteArrayOutputStream()
    val buffer = ByteArray(8192)
    var length: Int
    while (input.read(buffer).also { length = it } != -1) {
      result.write(buffer, 0, length)
    }
    return result.toString()
  }

  class GraphException(val traceId: String) : Throwable()

  companion object {
    private const val GRAPH_URL_SEND_MSG_BASE_FORMAT =
        "https://graph.facebook.com/app/send_notification"
    private const val GRAPH_URL_CREATE_ACCESS_TOKEN_BASE_FORMAT =
        "https://graph.facebook.com/oauth/access_token"

    private const val FIELD_PAYLOAD = "payload"
    private const val FIELD_TOKEN_ID = "token_id"
    private const val FIELD_CLIENT_ID = "client_id"
    private const val FIELD_ACCESS_TOKEN = "access_token"
    private const val FIELD_CLIENT_SECRET = "client_secret"
    private const val FIELD_DELIMITER = "&"

    private const val JSON_KEY_ERROR = "error"
    private const val JSON_KEY_FBTRACE_ID = "fbtrace_id"
    private const val JSON_KEY_TOKEN_ID = "token_id"
    private const val JSON_KEY_ACCESS_TOKEN = "access_token"
  }
}
