package psu.signlinguaasl.apiservice;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import psu.signlinguaasl.BuildConfig;
import psu.signlinguaasl.apiservice.middleware.AuthMiddleware;
import psu.signlinguaasl.apiservice.middleware.NgrokMiddleware;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static volatile Retrofit m_retrofit = null;
    private static volatile RetrofitClient m_instance;
    private static volatile String m_authToken;
    private static WifiManager m_wifiMan;

    private RetrofitClient() {}
    private static String BASE_URL;

    public static synchronized RetrofitClient getInstance(Context context)
    {
        if (m_instance == null)
        {
            m_instance = new RetrofitClient();
            m_authToken = AuthenticatedSession.getInstance(context.getApplicationContext()).getAuthToken();
            m_wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            BASE_URL = BuildConfig.BASE_URL;
            // Log.e("console", BASE_URL);
        }
        return m_instance;
    }

//    public String getBaseUrl()
//    {
//        WifiInfo wifiInf = m_wifiMan.getConnectionInfo();
//        int ipAddress = wifiInf.getIpAddress();
//        String ipv4 = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
//        String ip = String.format("%s%s:%s/api/", Routes.PROTOCOL, ipv4, Routes.PORT);
//        Log.e("console", "IP IS = " + ip);
//        return ip + "/api/";
//    }

    public synchronized Retrofit getClient()
    {
        if (m_retrofit == null)
        {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            if (BuildConfig.IS_DEVELOPMENT)
            {
                clientBuilder.addInterceptor(logging);
                clientBuilder.addInterceptor(new NgrokMiddleware());
            }

            // Add middleware only if token is available
            if (!TextUtils.isEmpty(m_authToken)) {
                clientBuilder.addInterceptor(new AuthMiddleware(m_authToken)).build();
            }

            m_retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
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
