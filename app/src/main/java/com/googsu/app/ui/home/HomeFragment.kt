package com.googsu.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.googsu.app.R
import com.googsu.app.SecondActivity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        kakaoLogin(root);
        return root
    }

    private fun kakaoLogin(root: View?) {
        val buttonkakaoLogin = root?.findViewById(R.id.kakao_login_button) as ImageButton
        val textHome = root?.findViewById(R.id.text_home) as TextView

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> { textHome.text = "접근이 거부 됨(동의 취소)" }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> { textHome.text = "유효하지 않은 앱" }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> { textHome.text = "인증 수단이 유효하지 않아 인증할 수 없는 상태" }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> { textHome.text = "요청 파라미터 오류" }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> { textHome.text = "유효하지 않은 scope ID" }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> { textHome.text = "설정이 올바르지 않음(android key hash)" }
                    error.toString() == AuthErrorCause.ServerError.toString() -> { textHome.text = "서버 내부 에러" }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> { textHome.text = "앱이 요청 권한이 없음" }
                    else -> { textHome.text = "기타 에러" }
                }
            }
            else if (token != null) {
                textHome.text = "로그인에 성공하였습니다."
                val intent = Intent(requireContext(), SecondActivity::class.java)
                startActivity(intent)
            }
        }

        buttonkakaoLogin.setOnClickListener {
            if(LoginClient.instance.isKakaoTalkLoginAvailable(requireContext())){
                LoginClient.instance.loginWithKakaoTalk(requireContext(), callback = callback)
            }else{
                LoginClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }
    }
}