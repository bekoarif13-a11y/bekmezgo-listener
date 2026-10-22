package com.bekmezgo.listener

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import kotlinx.coroutines.*
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class NotificationService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val packageName = sbn?.packageName ?: return
        if (packageName == "com.whatsapp" || packageName == "com.whatsapp.w4b") {
            val extras = sbn.notification.extras
            val title = extras.getString("android.title") ?: ""
            val text = extras.getCharSequence("android.text")?.toString() ?: ""

            val codePattern = Pattern.compile("BKMZ-[0-9]+")
            val matcher = codePattern.matcher(text)

            if (matcher.find()) {
                val extractedCode = matcher.group()
                val cleanPhone = title.replace(Regex("[^0-9]"), "")
                sendApiRequest(extractedCode, cleanPhone)
            }
        }
    }

    private fun sendApiRequest(code: String, phone: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://api.bekmezgo.com/integrations/whatsapp/approve")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("Authorization", "GBearer 23372c4b9dfb276ef4674d0325f14040c1d143156e4865eceec047e3946e4d56")
                conn.doOutput = true

                val jsonBody = "{\"code\": \"$code\", \"telefon\": \"$phone\", \"GercekNumara\": \"$phone\"}"

                val writer = OutputStreamWriter(conn.outputStream)
                writer.write(jsonBody)
                writer.flush()
                Log.d("API_RESPONSE", "Status: ${conn.responseCode}")
            } catch (e: Exception) {
                Log.e("API_ERROR", "${e.localizedMessage}")
            }
        }
    }
}