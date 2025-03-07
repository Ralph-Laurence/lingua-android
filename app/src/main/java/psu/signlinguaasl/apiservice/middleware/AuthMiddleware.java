package psu.signlinguaasl.apiservice.middleware;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthMiddleware implements Interceptor
{
    String authToken;

    public AuthMiddleware(String  authToken)
    {
        this.authToken = authToken;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + authToken)
                .build();

        return chain.proceed(newRequest);
    }
}
