package com.googsu.app

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import android.util.Log
import com.kakao.sdk.common.util.Utility

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "c9b98a7bff62a096ba071901a19a2d86")

        //var keyHash = Utility.getKeyHash(this)
        //Log.d("keyHash",keyHash)
    }
}