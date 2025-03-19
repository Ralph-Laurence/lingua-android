package psu.signlinguaasl.localservice.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import psu.signlinguaasl.apiservice.ResponseCode;
import psu.signlinguaasl.apiservice.RetrofitClient;
import psu.signlinguaasl.apiservice.auth.AuthService;
import psu.signlinguaasl.apiservice.auth.AuthResponse;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.models.Credentials;
import psu.signlinguaasl.localservice.models.User;
import psu.signlinguaasl.scene.LandingActivity;
import psu.signlinguaasl.ui.custom.Modal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController
{
    private Modal modal;
    private final Context context;

    public LoginController(Context context)
    {
        this.context = context;
        modal = new Modal(this.context);
    }

    public void InitiateLogin(String umail, String password, boolean rememberMe, Runnable loginBegan, Runnable loginEnd)
    {
        if (umail.isEmpty())
        {
            modal.ShowWarn("Please enter your username or email", "Oops!");
            return;
        }

        if (password.isEmpty())
        {
            modal.ShowWarn("Please enter your password", "Oops!");
            return;
        }

        loginBegan.run();

        AuthService auth = RetrofitClient.getInstance(context).getClient().create(AuthService.class);
        Map<String, String> credentials = new HashMap<>();

        credentials.put("umail", umail);
        credentials.put("password", password);

        Call<AuthResponse> call = auth.login(credentials);

        call.enqueue(new Callback<>()
        {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response)
            {
                int statusCode = response.code();

                if (response.isSuccessful() && statusCode == ResponseCode.OK)
                {
                    if (response.body() == null)
                    {
                        modal.ShowDanger("Sorry, we can't log you in due to a technical error. Please try again later.");
                        loginEnd.run();
                        return;
                    }

                    HandleSuccessfulLogin
                    (
                        response.body(),
                        new Credentials(umail, password),
                        rememberMe
                    );
                }
                else
                {
                    HandleFailedLogin(response);
                }

                loginEnd.run();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t)
            {
                Log.e("console", "Error: " + t.getMessage());
                modal.ShowDanger("Sorry, our service is unreachable right now. Please check your network connection and try again.");

                loginEnd.run();
            }
        });
    }

    private void HandleSuccessfulLogin(AuthResponse authResponse, Credentials credentials, boolean rememberMe)
    {
        // String message  = authResponse.getMessage();
        String token    = authResponse.getToken();
        User user       = authResponse.getUser();

        RetrofitClient.setAuthToken(token);

        // Save token securely using SharedPreferences or EncryptedSharedPreferences
        AuthenticatedSession session = AuthenticatedSession.getInstance(context);
        session.setSession(token, user);

        // Remember the user's login details to auto login him next time the app runs
        if (rememberMe)
        {
            session.rememberUserCredentials(credentials.getUmail(), credentials.getPassword());
            session.rememberUserId(user.getHashedId());
        }

        // Launch the home page (add the flags to prevent going back)
        Intent intent = new Intent(context, LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Then close the login screen
        if (context instanceof Activity)
        {
            ((Activity)context).finish();
        }
    }

    private void HandleFailedLogin(Response<AuthResponse> response)
    {
        // Capture the error message from the response body
        try
        {
            String errorBody = response.errorBody().string();
            JSONObject jsonObject = new JSONObject(errorBody);
            String errorMessage = jsonObject.getString("message");

            modal.ShowDanger(errorMessage, "Try Again");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            modal.ShowDanger("Our service isn't working right now. Please come back later.");
        }
    }
}
