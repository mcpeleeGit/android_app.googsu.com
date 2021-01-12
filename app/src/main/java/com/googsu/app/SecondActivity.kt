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
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.link.WebSharerClient
import com.kakao.sdk.story.StoryApiClient
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.Social
import com.kakao.sdk.user.UserApiClient

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // 토큰 정보 보기
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("accessTokenInfo", "토큰 정보 보기 실패", error)
                Toast.makeText(this, "토큰 정보 보기 실패 $error", Toast.LENGTH_SHORT).show()
            }
            else if (tokenInfo != null) {
                Log.i("accessTokenInfo", "토큰 정보 보기 성공" +
                        "\n회원번호: ${tokenInfo.id}" +
                        "\n만료시간: ${tokenInfo.expiresIn} 초")
                Toast.makeText(this, "토큰 정보 보기 성공\" +\n" +
                        "회원번호: ${tokenInfo.id}\" +\n" +
                        "만료시간: ${tokenInfo.expiresIn} 초", Toast.LENGTH_SHORT).show()
            }
        }
        // 카카오스토리 사용자 확인하기
        StoryApiClient.instance.isStoryUser { isStoryUser, error ->
            if (error != null) {
                Log.e("isStoryUser", "카카오스토리 사용자 확인 실패", error)
                Toast.makeText(this, "카카오스토리 사용자 확인 실패 $error", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.i("isStoryUser", "카카오스토리 가입 여부: $isStoryUser")
                Toast.makeText(this, "카카오스토리 가입 여부: $isStoryUser", Toast.LENGTH_SHORT).show()
            }
        }
        val kakao_story_profile_button: Button = findViewById(R.id.kakao_story_profile_button)
        val kakao_msg_api_button: Button = findViewById(R.id.kakao_msg_api_button)
        val kakao_link_msg_button: Button = findViewById(R.id.kakao_link_msg_button)
        val kakao_friends_list_button: Button = findViewById(R.id.kakao_friends_list_button)
        val kakao_talk_profile_button: Button = findViewById(R.id.kakao_talk_profile_button)
        val kakao_scope_button: Button = findViewById(R.id.kakao_scope_button)
        val kakao_logout_button: Button = findViewById(R.id.kakao_logout_button)
        val kakao_unlink_button: Button = findViewById(R.id.kakao_unlink_button)
        val id: TextView = findViewById(R.id.id)
        val nickname: TextView = findViewById(R.id.nickname)
        val profileimage_url: TextView = findViewById(R.id.profileimage_url)
        val thumbnailimage_url: TextView = findViewById(R.id.thumbnailimage_url)
        val defaultFeed = FeedTemplate(
                content = Content(
                        title = "딸기 치즈 케익",
                        description = "#케익 #딸기 #삼평동 #카페 #분위기 #소개팅",
                        imageUrl = "http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                        link = Link(
                                webUrl = "https://developers.kakao.com",
                                mobileWebUrl = "https://developers.kakao.com"
                        )
                ),
                social = Social(
                        likeCount = 286,
                        commentCount = 45,
                        sharedCount = 845
                ),
                buttons = listOf(
                        com.kakao.sdk.template.model.Button(
                                "웹으로 보기",
                                Link(
                                        webUrl = "https://developers.kakao.com",
                                        mobileWebUrl = "https://developers.kakao.com"
                                )
                        ),
                        com.kakao.sdk.template.model.Button(
                                "앱으로 보기",
                                Link(
                                        androidExecParams = mapOf("key1" to "value1", "key2" to "value2"),
                                        iosExecParams = mapOf("key1" to "value1", "key2" to "value2")
                                )
                        )
                )
        )

        kakao_story_profile_button.setOnClickListener {
            // 카카오스토리 프로필 가져오기
            StoryApiClient.instance.profile { profile, error ->
                if (error != null) {
                    Toast.makeText(this, "카카오스토리 프로필 가져오기 실패 $error", Toast.LENGTH_SHORT).show()
                }
                else if (profile != null) {
                    Toast.makeText(this, "카카오스토리 프로필 가져오기 성공\" +\n" +
                            "닉네임: ${profile.nickname}\" +\n" +
                            "프로필사진: ${profile.thumbnailUrl}\" +\n" +
                            "생일: ${profile.birthday}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        kakao_msg_api_button.setOnClickListener{
            TalkApiClient.instance.sendDefaultMemo(defaultFeed) { error ->
                if (error != null) {
                    Toast.makeText(this, "카카오톡 메세지 API 보내기 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "카카오톡 메세지 API 보내기 성공", Toast.LENGTH_SHORT).show()
                }
            }
        }

        kakao_link_msg_button.setOnClickListener {
            if (LinkClient.instance.isKakaoLinkAvailable(this)) {
                // 피드 메시지 보내기
                LinkClient.instance.defaultTemplate(this, defaultFeed) { linkResult, error ->
                    if (error != null) {
                        Toast.makeText(this, "카카오링크 보내기 실패 $error", Toast.LENGTH_SHORT).show()
                    }
                    else if (linkResult != null) {
                        Toast.makeText(this, "카카오링크 보내기 성공 ${linkResult.intent}", Toast.LENGTH_SHORT).show()
                        startActivity(linkResult.intent)

                        // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                        Toast.makeText(this, "Warning Msg: ${linkResult.warningMsg}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Argument Msg: ${linkResult.argumentMsg}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // 웹으로 공유
                WebSharerClient.instance.scrapTemplateUri("https://developers.kakao.com").let {

                    KakaoCustomTabsClient.openWithDefault(this, it)
                    // 또는
                    //startActivity(Intent(Intent.ACTION_VIEW, it))
                }
            }
        }

        kakao_friends_list_button.setOnClickListener {
            // 카카오톡 친구 목록 가져오기 (기본)
            TalkApiClient.instance.friends { friends, error ->
                if (error != null) {
                    Toast.makeText(this, "카카오톡 친구 목록 가져오기 실패 $error", Toast.LENGTH_SHORT).show()
                }
                else if (friends != null) {
                    Toast.makeText(this, "카카오톡 친구 목록 가져오기 성공 \n" +
                            "${friends.elements.joinToString("\n")}", Toast.LENGTH_SHORT).show()
                    // 친구의 UUID 로 메시지 보내기 가능
                }
            }
        }

        kakao_talk_profile_button.setOnClickListener {
// 카카오톡 프로필 가져오기
            TalkApiClient.instance.profile { profile, error ->
                if (error != null) {
                    Toast.makeText(this, "카카오톡 프로필 가져오기 실패 $error", Toast.LENGTH_SHORT).show()
                }
                else if (profile != null) {
                    Toast.makeText(this, "카카오톡 프로필 가져오기 성공\" +\n" +
                            "닉네임: ${profile.nickname}\" +\n" +
                            "프로필사진: ${profile.thumbnailUrl}\" +\n" +
                            "국가코드: ${profile.countryISO}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        kakao_scope_button.setOnClickListener {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Toast.makeText(this, "사용자 정보 요청 실패 $error", Toast.LENGTH_SHORT).show()
                }
                else if (user != null) {
                    if (user.kakaoAccount?.email != null) {
                        Toast.makeText(this, "이메일: ${user.kakaoAccount?.email}", Toast.LENGTH_SHORT).show()
                    }
                    else if (user.kakaoAccount?.emailNeedsAgreement == false) {
                        Toast.makeText(this, "사용자 계정에 이메일 없음. 꼭 필요하다면 동의항목 설정에서 수집 기능을 활성화 해보세요.", Toast.LENGTH_SHORT).show()
                    }
                    else if (user.kakaoAccount?.emailNeedsAgreement == true) {
                        Toast.makeText(this, "사용자에게 이메일 제공 동의를 받아야 합니다.", Toast.LENGTH_SHORT).show()
                        // 사용자에게 이메일 제공 동의 요청
                        val scopes = listOf("account_email,talk_message,friends")
                        LoginClient.instance.loginWithNewScopes(this, scopes) { token, error ->
                            if (error != null) {
                                Toast.makeText(this, "이메일 제공 동의 실패 $error", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "allowed scopes: ${token!!.scopes}", Toast.LENGTH_SHORT).show()

                                // 사용자 정보 재요청
                                UserApiClient.instance.me { user, error ->
                                    if (error != null) {
                                        Toast.makeText(this, "사용자 정보 요청 실패 $error", Toast.LENGTH_SHORT).show()
                                    }
                                    else if (user != null) {
                                        Toast.makeText(this, "이메일: ${user.kakaoAccount?.email}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

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