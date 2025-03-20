package psu.signlinguaasl.apiservice.users

import psu.signlinguaasl.apiservice.Routes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TutorsApiService
{
    // List all tutors EXCLUDING those already connected to the current learner
    @GET(Routes.findTutors)
    fun findTutors
    (
        @Query("page") page : Int,
        @Query("learnerId") learnerId : String,
        @Query("search") search : String?
    ) : Call<TutorsListApiResponse>

    // Fetch all tutors that belong to a specific learner,
    @GET(Routes.myTutors)
    fun myTutors
    (
        @Path("id") learnerId : String,
        @Query("page") page : Int,
        @Query("search") search : String?
    ) : Call<TutorsListApiResponse>

    // Show the full details of the selected tutor
    @GET(Routes.tutorDetails)
    fun showTutorDetails(@Path("id") id : String) : Call<TutorDetailsApiResponse>
}