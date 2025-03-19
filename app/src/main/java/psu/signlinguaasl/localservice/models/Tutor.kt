package psu.signlinguaasl.localservice.models

data class Tutor
(
    val tutorId         : String,
    val name            : String,
    val totalLearners   : Int,
    val ratings         : Float,
    val reviews         : Int,
    val disability      : Int,
    val photo           : String?,
    val bio             : String?,
    val disabilityBadge : String?,
    val disabilityDesc  : String?,
    val aboutMe         : String?,
    val contact         : String?,
    val address         : String?,
    val email           : String?,
    val education       : List<DocumentaryProof>?,
    val workExperience  : List<DocumentaryProof>?,
    val certifications  : List<DocumentaryProof>?
)
