package psu.signlinguaasl.apiservice.auth

import psu.signlinguaasl.apiservice.Routes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface FindTutorsApiService {
    @GET(Routes.findTutors)
    fun findTutors(@Query("page") page : Int, @Query("learnerId") learnerId : String) : Call<TutorResponse>
}