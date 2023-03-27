package com.mountainbook.mountainbook.user.dto

import jakarta.validation.constraints.*
import java.time.LocalDate

class UserDto {

    data class JoinDto(

        @NotBlank
        @Pattern(regexp = "[a-z0-9]{13}", message = "영소문자 숫자 포함 13자리를 입력해주세요.")
        var username: String,

        @NotBlank
        @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호가 최소 8자 이상이어야 하고 최소 하나의 문자와 하나의 숫자를 포함해야 합니다."
        )
        var password: String,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_-]{3,20}$", message = "닉네임은 3~20자 사이의 문자로만 가능합니다.")
        var nickname: String,

        @Email
        @NotBlank
        var email: String,

        @PastOrPresent
        @NotNull
        var birth: LocalDate
    )

    data class LoginDto(
        var username: String,
        var password: String
    )

    data class TokenDto(
        var token: String
    )

    data class StatusDto(
        var statusMessage: String
    )


}





