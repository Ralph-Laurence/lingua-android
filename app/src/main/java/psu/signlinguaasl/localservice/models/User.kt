package psu.signlinguaasl.localservice.models

data class User
(
    val id          : Int,
    val firstname   : String,
    val middlename  : String,
    val lastname    : String,
    val username    : String,
    val email       : String,
    val photo       : String,
    val role        : Int,
    val contact     : String,
    val address     : String,
)

