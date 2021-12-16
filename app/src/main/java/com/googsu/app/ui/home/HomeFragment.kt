package com.googsu.app.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.kakao.sdk.common.util.Utility
import android.widget.Toast
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler

import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.fragment_home.*


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
        kakaoLogin(root)

        return root
    }


    private fun kakaoLogin(root: View?) {
        val buttonkakaoLogin = root?.findViewById(R.id.kakao_login_button) as ImageButton
        val textHome = root?.findViewById(R.id.text_home) as TextView

        var keyHash = Utility.getKeyHash(requireContext())

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> { textHome.text = "AccessDenied :"+error.message }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> { textHome.text = "InvalidClient :"+error.message }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> { textHome.text = "InvalidGrant :"+error.message }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> { textHome.text = "InvalidRequest :"+error.message }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> { textHome.text = "InvalidScope :"+error.message }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> { textHome.text = "Misconfigured :"+error.message }
                    error.toString() == AuthErrorCause.ServerError.toString() -> { textHome.text = "ServerError :"+error.message }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> { textHome.text = "Unauthorized :"+error.message  }
                    else -> { textHome.text = keyHash+":"+error.message }
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