package psu.signlinguaasl.localservice.controllers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;
import java.util.function.Consumer;

import psu.signlinguaasl.apiservice.RetrofitClient;
import psu.signlinguaasl.apiservice.auth.FindTutorsApiService;
import psu.signlinguaasl.apiservice.auth.TutorResponse;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.models.Tutor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindTutorsController
{
    private int currentPage = 0;

    private FindTutorsApiService apiService;

    public Consumer<List<Tutor>> onTutorsRetrieved;
    public Consumer<String> onRetrieveFailed;
    private AuthenticatedSession authSession;

    public FindTutorsController(Context context)
    {
        authSession = AuthenticatedSession.getInstance(context);
        apiService  = RetrofitClient.getInstance(context)
                        .getClient()
                        .create(FindTutorsApiService.class);
    }

    public void fetchTutorsList()
    {
        currentPage++;

        String learnerHashedId = authSession.getUserId();

        Log.e("console", "Fetch began using learner id -> " + learnerHashedId);

        apiService.findTutors(currentPage, learnerHashedId).enqueue(new Callback<>()
        {
            @Override
            public void onResponse(Call<TutorResponse> call, Response<TutorResponse> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    Gson gson = new Gson();
                    String responseBody = gson.toJson(response.body());
                    Log.e("console", "Response body: " + responseBody);

                    if (response.body().data != null)
                        onTutorsRetrieved.accept(response.body().data.data);

                    // tutorList.addAll(response.body().data.data);
                    // adapter.notifyDataSetChanged();
                }
                else
                {
                    onRetrieveFailed.accept("Sorry, we're having issues right now. Please try again later");
                }
            }

            @Override
            public void onFailure(Call<TutorResponse> call, Throwable t)
            {
                // Handle failure
                onRetrieveFailed.accept("Sorry, our service is unreachable right now. Please check your network connection and try again.");
            }
        });
    }
}
