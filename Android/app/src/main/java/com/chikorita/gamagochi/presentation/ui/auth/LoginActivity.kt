package com.chikorita.gamagochi.presentation.ui.auth

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chikorita.gamagochi.R
import com.chikorita.gamagochi.databinding.ActivityLoginBinding
import com.chikorita.gamagochi.presentation.config.base.BaseActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate){

    override fun initView(){
        // 위치 권한 요청
        permission()

        /** HashKey확인 */
        val keyHash = Utility.getKeyHash(this)
        TextMsg(this, "HashKey: ${keyHash}")


        initListener()

        val circleImageView = binding.circleImage
        // 애니메이션 리소스 파일을 로드
        val scaleAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        // 이미지뷰에 애니메이션 적용
        circleImageView.startAnimation(scaleAnimation)

        val titleImageView = binding.titleLogo
        val scaleAnimation2: Animation = AnimationUtils.loadAnimation(this, R.anim.scale_up_down)
        titleImageView.startAnimation(scaleAnimation2)

        val logoImageView = binding.mudangLogo
        val rotateAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.rotate_logo)
        logoImageView.startAnimation(rotateAnimation)

    }
    private fun initListener(){
        /** Click_listener */
        binding.btnStartKakaoLogin.setOnClickListener {
            kakaoLogin() //로그인
        }
        binding.btnStartKakaoLogout.setOnClickListener {
            kakaoLogout() //로그아웃
        }
        binding.btnStartKakaoUnlink.setOnClickListener {
            kakaoUnlink() //연결해제
        }
    }

    private fun kakaoLogin() {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                TextMsg(this, "카카오계정으로 로그인 실패 : ${error}")
                setLogin(false, null)
            } else if (token != null) {
                //TODO: 최종적으로 카카오로그인 및 유저정보 가져온 결과
                UserApiClient.instance.me { user, error ->
                    TextMsg(this, "카카오계정으로 로그인 성공 \n\n " +
                            "token: ${token.accessToken} \n\n " +
                            "me: ${user}")
                    setLogin(true, token.accessToken)
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    TextMsg(this, "카카오톡으로 로그인 실패 : ${error}")

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    TextMsg(this, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    setLogin(true, token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun kakaoLogout(){
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                TextMsg(this, "로그아웃 실패. SDK에서 토큰 삭제됨: ${error}")
            }
            else {
                TextMsg(this, "로그아웃 성공. SDK에서 토큰 삭제됨")
                setLogin(false, null)
            }
        }
    }

    private fun kakaoUnlink(){
        // 연결 끊기
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                TextMsg(this, "연결 끊기 실패: ${error}")
            }
            else {
                TextMsg(this, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                setLogin(false, null)
            }
        }
    }

    private fun TextMsg(act: Activity, msg : String){
        binding.tvHashKey.text = msg
    }

    private fun setLogin(bool: Boolean, token: String?) {
        if (bool) {
            //TODO: 서버 통신 진행
//            overridePendingTransition(0, 0);
        }
    }

    private fun permission() {
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(this, locationPermission) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(locationPermission), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // 이미 권한이 있는 경우에 대한 처리
            // 예: 위치 정보 가져오기
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 승인된 경우에 대한 처리
                // 예: 위치 정보 가져오기
            } else {
                // 권한이 거부된 경우에 대한 처리
                // 예: 권한 거부 메시지 표시
                Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}