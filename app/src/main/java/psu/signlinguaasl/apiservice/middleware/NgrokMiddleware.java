package psu.signlinguaasl.apiservice.middleware;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NgrokMiddleware implements Interceptor
{
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException
    {
        Request original = chain.request();
        Request request = original.newBuilder()
                .addHeader("ngrok-skip-browser-warning", "true")
                .build();

        return chain.proceed(request);
    }
}
