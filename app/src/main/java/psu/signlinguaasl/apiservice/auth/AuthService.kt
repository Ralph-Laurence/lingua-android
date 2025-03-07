package psu.signlinguaasl.apiservice.auth
import psu.signlinguaasl.apiservice.Routes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST(Routes.login)
    fun login(@Body credentials: Map<String, String>): Call<AuthResponse>

    @POST(Routes.logout)
    fun logout(): Call<Void>
}