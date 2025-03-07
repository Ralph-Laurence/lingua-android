package psu.signlinguaasl.apiservice;

import android.content.Context;
import android.text.TextUtils;

import okhttp3.OkHttpClient;
import psu.signlinguaasl.apiservice.middleware.AuthMiddleware;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static volatile Retrofit m_retrofit = null;
    private static volatile RetrofitClient m_instance;
    private static volatile String m_authToken;

    private RetrofitClient() {}

    public static synchronized RetrofitClient getInstance(Context context)
    {
        if (m_instance == null) {
            m_instance = new RetrofitClient();
            m_authToken = AuthenticatedSession.getInstance(context.getApplicationContext()).getAuthToken();
        }
        return m_instance;
    }

    public synchronized Retrofit getClient()
    {
        if (m_retrofit == null)
        {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            // Add middleware only if token is available
            if (!TextUtils.isEmpty(m_authToken)) {
                clientBuilder.addInterceptor(new AuthMiddleware(m_authToken)).build();
            }

            m_retrofit = new Retrofit.Builder()
                    .baseUrl(Routes.BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return m_retrofit;
    }

    public static synchronized void setAuthToken(String token) {
        m_authToken = token;
        m_retrofit = null; // This will re-initialize retrofit with the new token
    }
}
