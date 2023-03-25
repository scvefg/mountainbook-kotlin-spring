package com.many.affection.board.dto

class PostDto {
    data class Request(
        var title: String,
        var contents: String,
        var tagList: Set<String>
    )
    data class Response(
        var username: String,
        var title: String,
        var contents: String,
        var views: Long,
        var tagSet: HashSet<String>
    )
}