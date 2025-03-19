package psu.signlinguaasl.localservice.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;
import java.util.function.Consumer;

import psu.signlinguaasl.apiservice.RetrofitClient;
import psu.signlinguaasl.apiservice.users.TutorsApiService;
import psu.signlinguaasl.apiservice.users.FindTutorApiResponse;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.models.Tutor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindTutorsController
{
    private int currentPage = 0;

    private TutorsApiService apiService;

    public Runnable onFetchBegan;
    public Consumer<List<Tutor>> onTutorsRetrieved;
    public Consumer<String> onRetrieveFailed;
    private AuthenticatedSession authSession;
    private int totalPages;

    public FindTutorsController(Context context)
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
        fetch(searchTerm);
    }

    private void fetch(String searchTerm)
    {
        String learnerHashedId = authSession.getUserId();

        if (onFetchBegan != null)
            onFetchBegan.run();

        apiService.findTutors(currentPage, learnerHashedId, searchTerm).enqueue(new Callback<>()
        {
            @Override
            public void onResponse(@NonNull Call<FindTutorApiResponse> call, Response<FindTutorApiResponse> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    Gson gson = new Gson();
                    String responseBody = gson.toJson(response.body());
                    Log.e("console", "Response body: " + responseBody);

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
                // Handle failure
                onRetrieveFailed.accept("Sorry, our service is unreachable right now. Please check your network connection and try again.");
            }
        });
    }

    public void setCurrentPage(int page) { currentPage = page; }
    public int getCurrentPage() { return currentPage; }

    public int getTotalPages() { return totalPages; }
}
