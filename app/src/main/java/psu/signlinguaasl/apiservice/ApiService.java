package psu.signlinguaasl.apiservice;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService
{
    @GET(Routes.androidTest)
    Call<ApiResponse> androidTest();
}
