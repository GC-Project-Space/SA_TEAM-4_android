package com.chikorita.gamagochi.data.dto

import com.chikorita.gamagochi.domain.model.MajorInfo

data class GetAllMajorResponse(
    val majorList : List<MajorInfo>
)