package psu.signlinguaasl.localservice.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import psu.signlinguaasl.apiservice.ResponseCode;
import psu.signlinguaasl.apiservice.RetrofitClient;
import psu.signlinguaasl.apiservice.auth.AuthResponse;
import psu.signlinguaasl.apiservice.auth.AuthService;
import psu.signlinguaasl.apiservice.users.TutorDetailsApiResponse;
import psu.signlinguaasl.apiservice.users.TutorsApiService;
import psu.signlinguaasl.localservice.models.Credentials;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.ui.custom.Modal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorDetailsController
{
    private Context m_context;

    public TutorDetailsController(Context context)
    {
        m_context = context;
    }

    public void fetch(String tutorId, Consumer<Tutor> onSuccess, Consumer<String> onFailed)
    {
        TutorsApiService auth = RetrofitClient.getInstance(m_context).getClient().create(TutorsApiService.class);
        Call<TutorDetailsApiResponse> call = auth.showTutorDetails(tutorId);

        call.enqueue(new Callback<>()
        {
            @Override
            public void onResponse(Call<TutorDetailsApiResponse> call, Response<TutorDetailsApiResponse> response)
            {
                int statusCode = response.code();

                if (response.isSuccessful() && statusCode == ResponseCode.OK)
                {
                    if (response.body() == null)
                    {
                        onFailed.accept("Sorry, we're unable to read the tutor's details due to a technical error. Please try again later.");
                        return;
                    }

                   onSuccess.accept(response.body().data);
                }
                else
                {
                    if (statusCode == ResponseCode.NOT_FOUND)
                        onFailed.accept("The tutor's record cannot be read or has already been removed.");
                    else
                        onFailed.accept("A problem has occurred while trying to retrieve the tutor's details. Please try again later.");
                }

                Log.e("console", String.format("Response with: %d", statusCode));
                Log.e("console", response.toString());
            }

            @Override
            public void onFailure(Call<TutorDetailsApiResponse> call, Throwable t)
            {
                Log.e("console", t.getMessage().toString());
                onFailed.accept("Sorry we're unable to retrieve the tutor's details. Please check your network connection then try again later.");
            }
        });
//        call.enqueue(new Callback<>()
//        {
//            @Override
//            public void onResponse(@NonNull Call<TutorDetailsApiResponse> call, @NonNull Response<TutorDetailsApiResponse> response)
//            {

//            }
//
//            @Override
//            public void onFailure(Call<AuthResponse> call, Throwable t)
//            {
//                Log.e("console", "Error: " + t.getMessage());
//                modal.ShowDanger("Sorry, our service is unreachable right now. Please check your network connection and try again.");
//
//                loginEnd.run();
//            }
//        });

    }
}
