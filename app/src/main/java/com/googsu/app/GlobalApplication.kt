package com.googsu.app

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.nhn.android.naverlogin.OAuthLogin
import okhttp3.*
import java.io.IOException


class GlobalApplication : Application() {
    private val client = OkHttpClient()
    override fun onCreate() {
        super.onCreate()

        //kakao
        KakaoSdk.init(this, "c9b98a7bff62a096ba071901a19a2d86")
        var keyHash = Utility.getKeyHash(this)
        Log.d("keyHash", keyHash)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token.toString()
            Log.d("FCM", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

            val encodemsg = java.net.URLEncoder.encode(msg, "utf-8")
            run("https://googsu.com/api/customer/app/excuteToken?token=$encodemsg")

            Log.d("api call", encodemsg)

            //var ANDROID_ID : String = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            var ANDROID_ID: String = FirebaseInstanceId.getInstance().getId()

            run("https://googsu.com/api/customer/app/excuteToken?token=$ANDROID_ID")




        })
    }
    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
        })
    }
}