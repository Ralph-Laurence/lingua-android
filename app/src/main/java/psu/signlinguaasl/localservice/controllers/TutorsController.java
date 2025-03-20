package psu.signlinguaasl.localservice.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;
import java.util.function.Consumer;

import psu.signlinguaasl.apiservice.RetrofitClient;
import psu.signlinguaasl.apiservice.users.TutorsApiService;
import psu.signlinguaasl.apiservice.users.TutorsListApiResponse;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.models.Tutor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorsController
{
    private int currentPage = 0;

    private final TutorsApiService apiService;
    private final AuthenticatedSession authSession;
    private int totalPages;

    public Runnable onFetchBegan;
    protected Runnable onFetchEnded;
    public Consumer<List<Tutor>> onTutorsRetrieved;
    public Consumer<String> onRetrieveFailed;

    public static final int FETCH_MODE_FIND_TUTORS = 574;
    public static final int FETCH_MODE_MY_TUTORS   = 295;
    
    public TutorsController(Context context)
    {
        authSession = AuthenticatedSession.getInstance(context);
        apiService  = RetrofitClient.getInstance(context)
                .getClient()
                .create(TutorsApiService.class);
    }

    public void fetchTutors(int fetchMode)
    {
        fetchTutors(fetchMode, null);
    }

    public void fetchTutors(int fetchMode, String searchTerm)
    {
        if (onFetchBegan != null)
            onFetchBegan.run();

        String learnerHashedId = authSession.getUserId();

        switch (fetchMode)
        {
            case FETCH_MODE_FIND_TUTORS:
                apiService.findTutors(currentPage, learnerHashedId, searchTerm).enqueue(onTutorsFetched);
                break;

            case FETCH_MODE_MY_TUTORS:
                apiService.myTutors(learnerHashedId, currentPage, searchTerm).enqueue(onTutorsFetched);
                break;
        }
    }

    private final Callback<TutorsListApiResponse> onTutorsFetched = new Callback<>()
    {
        @Override
        public void onResponse
        (
                @NonNull Call<TutorsListApiResponse> call,
                @NonNull Response<TutorsListApiResponse> response
            )
        {
            if (onFetchEnded != null)
                onFetchEnded.run();

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
        public void onFailure(@NonNull Call<TutorsListApiResponse> call, Throwable t)
        {
            if (onFetchEnded != null)
                onFetchEnded.run();

            // Handle failure
            if (onRetrieveFailed != null)
                onRetrieveFailed.accept("Sorry, our service is unreachable right now. Please check your network connection and try again.");
        }
    };

    public void setCurrentPage(int page) { currentPage = page; }
    public int getCurrentPage() { return currentPage; }

    public int getTotalPages() { return totalPages; }
}
