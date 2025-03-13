package psu.signlinguaasl;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import psu.signlinguaasl.apiservice.ApiResponse;
import psu.signlinguaasl.apiservice.ApiService;
import psu.signlinguaasl.apiservice.RetrofitClient;
import psu.signlinguaasl.apiservice.Routes;
import psu.signlinguaasl.apiservice.auth.AuthService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity
{
    private Button btnTestHttp;
    private TextView txvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        txvResponse = findViewById(R.id.txv_response);
        btnTestHttp = findViewById(R.id.btn_test_http);
        btnTestHttp.setOnClickListener(view -> TestHttp() );
    }

    private void TestHttp()
    {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Routes.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiService apiService = retrofit.create(ApiService.class);
//        Call<ApiResponse> call = apiService.androidTest();
//        call.enqueue(new Callback<ApiResponse>()
//        {
//            @Override
//            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    txvResponse.setText(response.body().getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse> call, Throwable t) {
//                txvResponse.setText("Error: " + t.getMessage());
//            }
//        });
    }
}