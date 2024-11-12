package com.chikorita.gamagochi.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.chikorita.gamagochi.data.dto.Mission
import com.chikorita.gamagochi.databinding.ActivityMainBinding
import com.chikorita.gamagochi.databinding.BottomsheetPersonalRankBinding
import com.chikorita.gamagochi.presentation.config.ApplicationClass
import com.chikorita.gamagochi.presentation.config.base.BaseBindingActivity
import com.chikorita.gamagochi.presentation.ui.ranking.RankingActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint

@AndroidEntryPoint
class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()

    private var kakaoMap: KakaoMap? = null

    private var uLatitude : Double = 0.0
    private var uLongitude : Double = 0.0

    private lateinit var personalRankingBottomSheet: BottomsheetPersonalRankBinding // 화면 아래에 표시되는 바텀시트
    private lateinit var behavior : BottomSheetBehavior<ConstraintLayout>

    private lateinit var handler: Handler

    private var isLocationUpdateEnabled = false // 버튼 클릭 상태 변수

    override fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 위치 권한 허용
        getLocationPermission()

        initMapSetting()

        // 지도가 현재 위치로 표시
        setCurrentLocation()
        // 하단 바텀 시트 표시
        setBottomSheet()

        // 위치 더미 데이터
//        addDummyMapData()

        setRankerBackground()
        initClickListener()

        viewModel.mission.observe(this) {
            val message = "미션 완료! 경험치가 올라요"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            ApplicationClass.missions = ApplicationClass.missions.map { missionId ->
                if (it.any { it == missionId }) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    0
                } else {
                    missionId
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.activityMainKakaoMap.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.activityMainKakaoMap.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    private fun sendLocationToServer() {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            val userNowLocation: Location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
            val latitude = userNowLocation.latitude
            val longitude = userNowLocation.longitude

            Log.d("Location_error","4")

            // 실제 서버 통신은 여기에 구현
            GlobalScope.launch {
                    Log.d("Location_error","5")
                    viewModel.postLocation(latitude, longitude, ApplicationClass.missions)
                    Log.d("Location_error","6")
            }
            Log.d("Location_error","7")

            Log.d("LOCATION_UPDATE", "Latitude: $latitude, Longitude: $longitude")
        } catch (e: SecurityException) {
            Log.e("LOCATION_ERROR", e.toString())
            Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("LOCATION_ERROR", e.toString())
        }
    }

    private fun initClickListener(){
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

        binding.activityMainFeedBtn.setOnClickListener {
            if (isLocationUpdateEnabled) {
                // 버튼을 눌렀을 때 위치 정보 업데이트가 활성화되어 있으면 비활성화
                stopLocationUpdates()
                binding.activityMainFeedBtn.text = "무당이 밥 먹이기"
            } else {
                // 버튼을 눌렀을 때 위치 정보 업데이트가 비활성화되어 있으면 활성화
                startLocationUpdates()
                binding.activityMainFeedBtn.text = "무당이는 밥 먹는 중"
            }
            isLocationUpdateEnabled = !isLocationUpdateEnabled
        }

    }

    private fun startLocationUpdates() {
        Log.d("Location_error","1")
        handler = Handler(Looper.getMainLooper())
        handler.post(locationUpdateRunnable)
    }

    private fun stopLocationUpdates() {
        handler.removeCallbacks(locationUpdateRunnable)
    }

    private val locationUpdateRunnable = object : Runnable {
        override fun run() {
            Log.d("Location_error","2")

            sendLocationToServer()
            Log.d("Location_error","3")

            handler.postDelayed(this, 60000) // 1분마다 실행
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
                setCurrentLocation()
            }
        })
    }

    private fun setCurrentLocation() {
        val currentLatLng = LatLng.from(uLatitude, uLongitude)
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(currentLatLng, DEFAULT_ZOOM_LEVEL)
        kakaoMap?.moveCamera(cameraUpdate)

        addCurrentLocationMarker(currentLatLng) // 현재 위치 마커 표시
        Log.d("MainActivity", currentLatLng.toString())
    }

    private fun addCurrentLocationMarker(currentLatLng: LatLng) {
        // 마커 스타일 지정
        val styles = kakaoMap?.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(R.drawable.ic_custom_marker)))
        // 지도에 마커 추가
        kakaoMap?.labelManager?.layer?.addLabel(
            LabelOptions.from(currentLatLng).setStyles(styles)
        )?.show()
    }

    private fun setMissionMark(dataList: ArrayList<Mission>) {
        for (data in dataList) {
            val marker = MapPOIItem()
            marker.apply {
                itemName = data.missionName
                mapPoint = MapPoint.mapPointWithGeoCoord(data.latitude, data.longitude)
//                markerType = MapPOIItem.MarkerType.BluePin
                selectedMarkerType = MapPOIItem.MarkerType.RedPin
            }
//            mapView.addPOIItem(marker)
        }
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

    private fun sendInfoToServer() {
        // 서버 통신을 별도의 쓰레드에서 처리
        Thread(Runnable {
            getLocationPermission()
            //viewModel.postLocation(uLatitude, uLongitude, ApplicationClass.missions)

        }).start()
    }

    companion object {
        const val DEFAULT_ZOOM_LEVEL = 17 // 기본 줌 레벨
    }
}