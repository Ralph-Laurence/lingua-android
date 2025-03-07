package psu.signlinguaasl.apiservice

object ResponseCode {
    const val OK            : Int = 200
    const val CREATED       : Int = 201
    const val NO_CONTENT    : Int = 204

    // CLIENT ERROR RESPONSES
    const val UNAUTHORIZED  : Int = 401
    const val FORBIDDEN     : Int = 403
    const val BAD_REQUEST   : Int = 400
    const val NOT_FOUND     : Int = 404

    // SERVER ERROR RESPONSE
    const val INTERNAL_SERVER_ERROR : Int = 500;
}