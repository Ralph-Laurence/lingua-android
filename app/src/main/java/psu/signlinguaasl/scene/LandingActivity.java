package psu.signlinguaasl.scene;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import psu.signlinguaasl.R;

import psu.signlinguaasl.subscenes.HomeSubScene;
import psu.signlinguaasl.ui.custom.NavBanner;

public class LandingActivity extends AppCompatActivity// BaseAuthenticatedActivity
{
    // private TextView welcomeText;
//
//    @Override
//    protected void OnAwake()
//    {
//
//    }
//
//    @Override
//    protected int useLayout()
//    {
//        return R.layout.activity_home;
//    }
//
//    @Override
//    protected void OnCreateViews()
//    {
//        welcomeText = findViewById(R.id.home_welcome_text);
//
//        AuthenticatedSession session = AuthenticatedSession.getInstance(this);
//
//        String msg = String.format("Welcome home, %s ... Glad to be back?", session.getUser().getFirstname());
//        welcomeText.setText(msg);
//    }

    private NavBanner navBanner;
    private HomeSubScene homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navBanner = findViewById(R.id.landing_nav_banner);
        navBanner.setOnNavItemSelected(frame -> {
            Toast.makeText(this, "Scroll To: " + frame, Toast.LENGTH_SHORT).show();
        });

        homeFragment = new HomeSubScene();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.landing_subscenes_container, homeFragment)
                .commit();
    }
}