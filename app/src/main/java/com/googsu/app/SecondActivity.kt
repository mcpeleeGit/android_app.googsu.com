package com.googsu.app

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.kakao.sdk.user.UserApiClient

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // 토큰 정보 보기
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("accessTokenInfo", "토큰 정보 보기 실패", error)
            }
            else if (tokenInfo != null) {
                Log.i("accessTokenInfo", "토큰 정보 보기 성공" +
                        "\n회원번호: ${tokenInfo.id}" +
                        "\n만료시간: ${tokenInfo.expiresIn} 초")
            }
        }


        val kakao_logout_button: Button = findViewById(R.id.kakao_logout_button)
        val kakao_unlink_button: Button = findViewById(R.id.kakao_unlink_button)
        val id: TextView = findViewById(R.id.id)
        val nickname: TextView = findViewById(R.id.nickname)
        val profileimage_url: TextView = findViewById(R.id.profileimage_url)
        val thumbnailimage_url: TextView = findViewById(R.id.thumbnailimage_url)

        kakao_logout_button.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
            }
        }

        kakao_unlink_button.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(this, "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
                }
            }
        }
        // 프로필 정보
        UserApiClient.instance.me { user, error ->
            id.text = "회원번호: ${user?.id}"
            nickname.text = "닉네임: ${user?.kakaoAccount?.profile?.nickname}"
            profileimage_url.text = "프로필 링크: ${user?.kakaoAccount?.profile?.profileImageUrl}"
            thumbnailimage_url.text = "썸네일 링크: ${user?.kakaoAccount?.profile?.thumbnailImageUrl}"
        }
    }

}