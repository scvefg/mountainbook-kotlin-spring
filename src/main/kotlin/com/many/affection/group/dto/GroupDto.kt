package com.many.affection.group.dto

import java.time.LocalDate

class GroupDto {

    data class RequestDto(
        var fee: Long,
        var total: Long,
        var period: LocalDate,
        var place: String
    )

    data class ResponseDto(
        var fee: Long,
        var total: Long,
        var current: Long,
        var period: String,
        var place: String,
        var leader: String,
        var memberList: List<String>,

    )

}