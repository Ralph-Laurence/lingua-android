package psu.signlinguaasl.apiservice

object Routes {
    const val HOST          : String = "http://192.168.42.115"
    const val PORT          : Int = 8000
    const val BASE_URL      : String = "$HOST:$PORT/api/"

    // END POINTS
    const val androidTest   : String = "androidtest"
    const val login         : String = "login"
    const val logout        : String = "logout"
}