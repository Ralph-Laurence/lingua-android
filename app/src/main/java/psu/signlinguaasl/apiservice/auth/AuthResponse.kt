package psu.signlinguaasl.apiservice.auth

import psu.signlinguaasl.localservice.models.User

data class AuthResponse(
    val token   : String,
    val user    : User,
    val message : String,
)