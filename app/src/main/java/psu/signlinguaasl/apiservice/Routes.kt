package psu.signlinguaasl.apiservice

object Routes {

    // const val BASE_URL = "https://remarkably-patient-wildcat.ngrok-free.app/api/"

    // END POINTS
    const val androidTest   : String = "androidtest"
    const val login         : String = "login"
    const val logout        : String = "logout"

    const val findTutors    : String = "signlingua/tutors"
    const val tutorDetails  : String = "signlingua/tutors/{id}"
    const val myTutors      : String = "signlingua/learners/{id}/tutors"
}