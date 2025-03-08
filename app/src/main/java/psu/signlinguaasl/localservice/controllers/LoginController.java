package psu.signlinguaasl.localservice.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import psu.signlinguaasl.apiservice.ResponseCode;
import psu.signlinguaasl.apiservice.RetrofitClient;
import psu.signlinguaasl.apiservice.auth.AuthService;
import psu.signlinguaasl.apiservice.auth.AuthResponse;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.models.User;
import psu.signlinguaasl.localservice.security.SecureAuthToken;
import psu.signlinguaasl.scene.HomeActivity;
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

    public void InitiateLogin(String umail, String password, Runnable loginBegan, Runnable loginEnd)
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
                    AuthResponse authResponse = response.body();
                    HandleSuccessfulLogin(authResponse);
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
                // Log.e("Login", "Error: " + t.getMessage());
                modal.ShowDanger("Sorry, our service is unreachable right now. Please check your network connection and try again.");

                loginEnd.run();
            }
        });
    }

    private void HandleSuccessfulLogin(AuthResponse authResponse)
    {
        String token    = authResponse.getToken();
        String message  = authResponse.getMessage();
        User user       = authResponse.getUser();

        RetrofitClient.setAuthToken(token);

        // Save token securely using SharedPreferences or EncryptedSharedPreferences
        AuthenticatedSession session = AuthenticatedSession.getInstance(context);
        session.setSession(token, user);

        // Launch the home page (add the flags to prevent going back)
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Then close the login screen
        if (context instanceof Activity)
        {
            ((Activity)context).finish();
        }
        // StringBuilder sb = new StringBuilder();
        // sb.append(message);
        // sb.append("\n\nWith token: ");
        // sb.append(tokenStore.getToken());
        // sb.append("\n\nWelcome, ");
        // sb.append(user.getFirstname());
        // String msg = sb.toString();

        // Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();
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
            modal.ShowDanger("The server isn't working right now. Please come back later.");
        }
    }
}
