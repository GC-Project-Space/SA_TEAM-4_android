package com.chikorita.gamagochi.domain.model

import com.google.gson.annotations.SerializedName

data class MajorInfo(
    @SerializedName("major")
    val name : String,
    @SerializedName("experience_points")
    val totalExperience : Long
)

data class MajorName(
    val name : String
)

data class MajorRanker(
    var rank: Int,
    var major: String,
    var exp: Long
)
