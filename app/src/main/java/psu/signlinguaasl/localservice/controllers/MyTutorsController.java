package psu.signlinguaasl.localservice.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;
import java.util.function.Consumer;

import psu.signlinguaasl.apiservice.RetrofitClient;
import psu.signlinguaasl.apiservice.users.FindTutorApiResponse;
import psu.signlinguaasl.apiservice.users.TutorsApiService;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.models.Tutor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTutorsController
{
    private int currentPage = 0;

    private TutorsApiService apiService;

    public Runnable onFetchBegan;
    public Consumer<List<Tutor>> onTutorsRetrieved;
    public Consumer<String> onRetrieveFailed;
    private AuthenticatedSession authSession;
    private int totalPages;

    public MyTutorsController(Context context)
    {
        authSession = AuthenticatedSession.getInstance(context);
        apiService  = RetrofitClient.getInstance(context)
                .getClient()
                .create(TutorsApiService.class);
    }

    public void fetchTutorsList()
    {
        fetch(null);
    }

    public void fetchTutorsList(String searchTerm)
    {
        // currentPage = 0;
        fetch(searchTerm);
    }

    private void fetch(String searchTerm)
    {
        // currentPage++;

        String learnerHashedId = authSession.getUserId();

//        Log.e("console", "Fetch began using learner id -> " + learnerHashedId);
//
//        if (!Str.IsNullOrEmpty(searchTerm))
//            Log.e("console", "Search For -> " + searchTerm);


        if (onFetchBegan != null)
            onFetchBegan.run();

        apiService.myTutors(learnerHashedId, currentPage, searchTerm).enqueue(new Callback<>()
        {
            @Override
            public void onResponse(@NonNull Call<FindTutorApiResponse> call, Response<FindTutorApiResponse> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    Gson gson = new Gson();
                    String responseBody = gson.toJson(response.body());
                    Log.d("console", "Response body: " + responseBody);

                    if (onTutorsRetrieved != null)
                        onTutorsRetrieved.accept(response.body().data.data);
                }
                else
                {
                    if (onRetrieveFailed != null)
                        onRetrieveFailed.accept("Sorry, we're having issues right now. Please try again later");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FindTutorApiResponse> call, Throwable t)
            {
                Log.e("console", t.getMessage());
                // Handle failure
                onRetrieveFailed.accept("Sorry, our service is unreachable right now. Please check your network connection and try again.");
            }
        });
    }
}
