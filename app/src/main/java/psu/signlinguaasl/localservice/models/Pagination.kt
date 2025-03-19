package psu.signlinguaasl.localservice.models

data class Pagination
(
    val current_page    : Int,
    val last_page       : Int,
    val next_page_url   : String?,
    val prev_page_url   : String?
)