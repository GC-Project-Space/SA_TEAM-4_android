package com.chikorita.gamagochi.presentation.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chikorita.gamagochi.data.dto.LadybugDetailResult
import com.chikorita.gamagochi.data.dto.RankingList
import com.chikorita.gamagochi.domain.model.MajorInfo
import com.chikorita.gamagochi.domain.model.MajorRanker
import com.chikorita.gamagochi.domain.model.Mission
import com.chikorita.gamagochi.domain.model.MissionStatus
import com.chikorita.gamagochi.domain.model.SchoolRanker
import com.chikorita.gamagochi.domain.repository.MissionRepository
import com.chikorita.gamagochi.domain.repository.RankRepository
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val missionRepository: MissionRepository,
    private val levelRepository: RankRepository,
) : ViewModel(){

    var currentPathIndex = 0 // 이동 경로의 현재 인덱스

    var MajorRankerArray = arrayListOf(
        MajorRanker(1, "소프트웨어", 450000),
        MajorRanker(2, "인공지능", 390000),
        MajorRanker(3, "컴퓨터공학", 380000)
    )
    var SchoolRankerArray = arrayListOf(
        SchoolRanker(16, "로건", "무당신", 3400),
        SchoolRanker(17, "마라", "무당짱", 2300),
        SchoolRanker(18, "코코아", "무당이", 1700)
    )

    var movePath = listOf(
        LatLng.from(37.455105, 127.135176), // 운동장
        LatLng.from(37.455204, 127.135067),
        LatLng.from(37.455152, 127.134981),
        LatLng.from(37.455172, 127.134675),
        LatLng.from(37.455205, 127.134419), // 공학관 입성
        LatLng.from(37.455198, 127.134104),
        LatLng.from(37.455196, 127.133963)
    )

    private val _rankingList = MutableLiveData<ArrayList<RankingList>>()
    val rankingList : LiveData<ArrayList<RankingList>>
        get() = _rankingList

    private val _ladybug = MutableLiveData<LadybugDetailResult>()
    val ladybug : LiveData<LadybugDetailResult>
        get() = _ladybug

    private val _majorList = MutableLiveData<ArrayList<MajorInfo>>()
    val majorList : LiveData<ArrayList<MajorInfo>>
        get() = _majorList

    private val _missions = MutableLiveData<List<Mission>>()
    val missions : LiveData<List<Mission>>
        get() = _missions

    private val _missionStatus = MutableLiveData<MissionStatus>(MissionStatus.UNABLE)
    val missionStatus: LiveData<MissionStatus>
        get() = _missionStatus

    private val _nearbyMission = MutableLiveData<Mission>()
    val nearbyMission: LiveData<Mission>
        get() = _nearbyMission

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    /** 미션 목록 조회 (지도 화면) */
    fun getMissions() {
        viewModelScope.launch {
            _missions.value = missionRepository.getMissionList()
        }
    }

    /** 미션 성공 처리 */
    fun clearMission() {
        viewModelScope.launch(Dispatchers.IO) {
            _isSuccess.postValue(missionRepository.clearMission(_nearbyMission.value!!.missionId))
        }
    }

    /** 내 정보 조회 */
    fun getLadybugDetail() {
        viewModelScope.launch(Dispatchers.IO) {
            _ladybug.value = levelRepository.getLadybugDetail().result
        }
    }

    fun getLevelRanking() {
        viewModelScope.launch(Dispatchers.IO) {
            levelRepository.getLevelRanking().result.rankingList.let {
                if (it.isNotEmpty()) _rankingList.value = it as ArrayList
            }
            Log.d("MainViewModel", "rankingList: ${_rankingList.value}")
        }
    }

    fun getAllMajor() {
        viewModelScope.launch(Dispatchers.IO) {
            levelRepository.getMajorAll().majorList.let {
                if (it.isNotEmpty()) _majorList.value = it as ArrayList
            }
            Log.d("MainViewModel", "majorList: ${_majorList.value}")
        }
    }

    fun setNearbyMission(currentLatLng: LatLng) {
        _nearbyMission.value = missions.value!!.minByOrNull { calculateDistance(currentLatLng, it.latLng) }
        updateMissionStatus(MissionStatus.ABLE)
    }

    fun updateMissionStatus(status: MissionStatus) {
        _missionStatus.value = status
    }

    // 두 LatLng 간의 거리를 계산하는 함수 (단위: 미터)
    private fun calculateDistance(latLng1: LatLng, latLng2: LatLng): Double {
        val earthRadius = 6371000.0 // 지구 반지름 (단위: 미터)
        val lat1Rad = Math.toRadians(latLng1.latitude)
        val lat2Rad = Math.toRadians(latLng2.latitude)
        val deltaLat = Math.toRadians(latLng2.latitude - latLng1.latitude)
        val deltaLng = Math.toRadians(latLng2.longitude - latLng1.longitude)

        val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }

//    fun postLocation(latitude : Double, longitude : Double, mission : List<Long>) {
//        val ladybugLocationRequest = LadybugLocationRequest(latitude, longitude, mission)
//        Log.d("Location_error", ladybugLocationRequest.toString())
//
//        viewModelScope.launch(Dispatchers.IO) {
//            _mission.value = levelRepository.postLocation(ladybugLocationRequest).result.successMission
//        }
//    }

}