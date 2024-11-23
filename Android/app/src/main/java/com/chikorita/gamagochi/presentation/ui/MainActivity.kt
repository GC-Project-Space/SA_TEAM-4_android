package com.chikorita.gamagochi.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chikorita.gamagochi.R
import com.chikorita.gamagochi.databinding.ActivityMainBinding
import com.chikorita.gamagochi.databinding.BottomsheetPersonalRankBinding
import com.chikorita.gamagochi.domain.model.Mission
import com.chikorita.gamagochi.domain.model.MissionStatus
import com.chikorita.gamagochi.presentation.config.base.BaseBindingActivity
import com.chikorita.gamagochi.presentation.ui.ranking.RankingActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.label.LabelTextStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()

    private var kakaoMap: KakaoMap? = null

    private var uLatitude : Double = 0.0
    private var uLongitude : Double = 0.0

    private lateinit var personalRankingBottomSheet: BottomsheetPersonalRankBinding // 화면 아래에 표시되는 바텀시트
    private lateinit var behavior : BottomSheetBehavior<ConstraintLayout>

    // 기존 마커를 저장하는 변수
    private var currentLocationMarker: Label? = null

    override fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 위치 권한 허용
        getLocationPermission()

        initMapSetting()

        // 하단 바텀 시트 표시
        setBottomSheet()

        setRankerBackground()
        initClickListener()
        initObserve()
    }

    override fun onStart() {
        super.onStart()

        viewModel.getMissions() // 미션 목록 조회
    }

    override fun onResume() {
        super.onResume()
        binding.activityMainKakaoMap.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.activityMainKakaoMap.pause()
    }

    private fun initClickListener() {
        // 미션 달성 버튼
        binding.activityMainFeedBtn.setOnClickListener {
            viewModel.clearMission()
        }

        personalRankingBottomSheet = binding.activityMainBottom

        val intent = Intent(this, RankingActivity::class.java)
        val bundle = Bundle()

        personalRankingBottomSheet.schoolRankingBtn.setOnClickListener {
            bundle.putString("key","school")
            intent.putExtra("bundle",bundle)
            startActivity(intent)
        }
        personalRankingBottomSheet.majorRankingBtn.setOnClickListener {
            bundle.putString("key","major")
            intent.putExtra("bundle",bundle)
            startActivity(intent)
        }
    }

    private fun initObserve() {
        viewModel.missions.observe(this) { missions ->
            Log.d("MainActivity", "missionList: $missions")
            if (missions.isNotEmpty()) {
                kakaoMap?.labelManager?.layer?.removeAll()
                missions.forEach {
                    addMissionMarker(it) // 지도에 마커 추가
                }
            }
        }

        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "미션 달성 성공!", Toast.LENGTH_SHORT).show()
                viewModel.updateMissionStatus(MissionStatus.DONE) // 버튼 상태 변경
                viewModel.getMissions() // 미션 목록 업데이트
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setRankerBackground() {
        val bottomDialog = binding.activityMainBottom

        with(viewModel) {
            if (SchoolRankerArray[0].nickName == "로건") {
                bottomDialog.rankUser0.root.setBackgroundResource(R.drawable.gradation_rectangle_2)
                bottomDialog.rankUser1.root.setBackgroundResource(R.drawable.border_rectangle)
                bottomDialog.rankUser2.root.setBackgroundResource(R.drawable.border_rectangle)

            } else if (SchoolRankerArray[2].nickName == "로건") {
                bottomDialog.rankUser0.root.setBackgroundResource(R.drawable.border_rectangle)
                bottomDialog.rankUser1.root.setBackgroundResource(R.drawable.border_rectangle)
                bottomDialog.rankUser2.root.setBackgroundResource(R.drawable.gradation_rectangle_2)
            } else {
                bottomDialog.rankUser0.root.setBackgroundResource(R.drawable.border_rectangle)
                bottomDialog.rankUser1.root.setBackgroundResource(R.drawable.gradation_rectangle_2)
                bottomDialog.rankUser2.root.setBackgroundResource(R.drawable.border_rectangle)
            }


            if (MajorRankerArray[0].major == "소프트웨어") {
                bottomDialog.rankMajor0.root.setBackgroundResource(R.drawable.gradation_rectangle_2)
                bottomDialog.rankMajor1.root.setBackgroundResource(R.drawable.border_rectangle)
                bottomDialog.rankMajor2.root.setBackgroundResource(R.drawable.border_rectangle)

            } else if (MajorRankerArray[2].major == "소프트웨어") {
                bottomDialog.rankMajor0.root.setBackgroundResource(R.drawable.border_rectangle)
                bottomDialog.rankMajor1.root.setBackgroundResource(R.drawable.border_rectangle)
                bottomDialog.rankMajor2.root.setBackgroundResource(R.drawable.gradation_rectangle_2)
            } else {
                bottomDialog.rankMajor0.root.setBackgroundResource(R.drawable.border_rectangle)
                bottomDialog.rankMajor1.root.setBackgroundResource(R.drawable.gradation_rectangle_2)
                bottomDialog.rankMajor2.root.setBackgroundResource(R.drawable.border_rectangle)
            }
        }

    }

    private fun getLocationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            val userNowLocation : Location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
            uLatitude = userNowLocation.latitude
            uLongitude = userNowLocation.longitude
        } catch (e : NullPointerException) {
            Log.e("LOCATION_ERROR", e.toString())
            ActivityCompat.finishAffinity(this)

            finish()
        }
    }

    private fun initMapSetting() {
        binding.activityMainKakaoMap.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출
                Log.d("KakaoMap", "onMapDestroy: ")
            }
            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                Log.d("KakaoMap", "onMapError: $error")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                Log.d("KakaoMap", "onMapReady: $kakaoMap")
                this@MainActivity.kakaoMap = kakaoMap
                setCurrentLocation() // 지도가 현재 위치로 표시
                viewModel.missions.value?.forEach {
                    addMissionMarker(it)
                }
            }
        })
    }

    private fun moveAlongPath(movePath: List<LatLng>) {
        val handler = Handler(Looper.getMainLooper())
        viewModel.currentPathIndex = 0

        val moveRunnable = object : Runnable {
            override fun run() {
                if (viewModel.currentPathIndex < movePath.size) {
                    val targetLatLng = movePath[viewModel.currentPathIndex]

                    // 기존 마커 위치 업데이트
                    addOrUpdateCurrentLocationMarker(targetLatLng)

                    Log.d("MainActivity", "Moved to: $targetLatLng")

                    // 다음 경로로 이동
                    viewModel.currentPathIndex++
                    handler.postDelayed(this, DELAY_MILLS)
                    if (viewModel.currentPathIndex == movePath.size) {
                        viewModel.setNearbyMission(movePath.last()) // 마지막 미션일 경우 미션 달성 버튼 활성화
                    }
                } else {
                    handler.removeCallbacks(this)
                    Log.d("MainActivity", "Path movement completed.")
                }
            }
        }
        handler.post(moveRunnable)
    }


    // movePath로 이동하도록 수정
    private fun setCurrentLocation() {
        if (viewModel.movePath.isNotEmpty()) {
            moveAlongPath(viewModel.movePath) // movePath를 따라 이동 시작
        }
    }

    // 마커 생성 또는 위치 업데이트
    private fun addOrUpdateCurrentLocationMarker(currentLatLng: LatLng) {
        if (currentLocationMarker == null) {
            // 마커가 없으면 새로 생성
            val styles = kakaoMap?.labelManager?.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(R.drawable.ic_custom_marker)
                )
            )
            // 지도에 마커 추가
            currentLocationMarker = kakaoMap?.labelManager?.layer?.addLabel(
                LabelOptions.from(currentLatLng).setStyles(styles)
            )?.apply { show() }
            // 지도 중심 이동
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(currentLatLng, DEFAULT_ZOOM_LEVEL)
            kakaoMap?.moveCamera(cameraUpdate)
        } else {
            // 기존 마커의 위치를 업데이트
            currentLocationMarker?.moveTo(currentLatLng)
        }
    }

    private fun addMissionMarker(mission: Mission) {
        // 지도에 미션 마커 추가
        kakaoMap?.labelManager?.layer?.addLabel(LabelOptions.from(mission.latLng)
            .setStyles(setPinStyle(mission.isFinished))
            .setTexts(
                LabelTextBuilder().setTexts(mission.missionName)
            )
        )
    }

    private fun setBottomSheet() {
        behavior = BottomSheetBehavior.from(binding.activityMainBottom.dialogMapBehaviorView)

    }

    private suspend fun setDataView() {
        withContext(Dispatchers.Main) {
            with(viewModel){
                val bottomDialog = binding.activityMainBottom

                ladybug.observe(this@MainActivity) {

                    bottomDialog.majorTv.text = it.majorType
                    bottomDialog.symbolTv.text = it.symbol
                }

                getLadybugDetail()
            }
        }
    }

    companion object {
        const val DEFAULT_ZOOM_LEVEL = 17 // 기본 줌 레벨
        const val DELAY_MILLS = 2500L // 5초마다 이동

        fun setPinStyle(isFinishedMission: Boolean): LabelStyles {
            return LabelStyles.from(
                LabelStyle.from(if (isFinishedMission) R.drawable.ic_pin_finished else R.drawable.ic_pin_default)
                    .setTextStyles(
                        LabelTextStyle.from(24, Color.BLACK)
                    )
            )
        }
    }
}