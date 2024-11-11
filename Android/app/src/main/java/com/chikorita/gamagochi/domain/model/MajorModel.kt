package com.chikorita.gamagochi.domain.model

data class MajorInfo(
    val name : String,
    val totalExperience : String
)

data class MajorName(
    val name : String
)

data class MajorRanker(
    var rank: Int,
    var major: String,
    var exp: Long
)
