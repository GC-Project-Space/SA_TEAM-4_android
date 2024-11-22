package com.chikorita.gamagochi.presentation.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chikorita.gamagochi.data.dto.LadybugDetailResult
import com.chikorita.gamagochi.data.dto.LadybugLocationRequest
import com.chikorita.gamagochi.data.dto.RankingList
import com.chikorita.gamagochi.domain.model.MajorInfo
import com.chikorita.gamagochi.domain.model.MajorRanker
import com.chikorita.gamagochi.domain.model.Mission
import com.chikorita.gamagochi.domain.model.SchoolRanker
import com.chikorita.gamagochi.domain.repository.MissionRepository
import com.chikorita.gamagochi.domain.repository.RankRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val missionRepository: MissionRepository,
    private val levelRepository: RankRepository,
) : ViewModel(){

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

    fun getMissions() {
        viewModelScope.launch {
            _missions.value = missionRepository.getMissionList()
        }
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