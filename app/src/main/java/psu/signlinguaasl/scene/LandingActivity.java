package psu.signlinguaasl.scene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import psu.signlinguaasl.R;

import psu.signlinguaasl.AuthenticatedActivity;
import psu.signlinguaasl.localservice.interfaces.OnFragmentBackPressedListener;
import psu.signlinguaasl.subscenes.HomeSubScene;
import psu.signlinguaasl.ui.custom.NavBanner;

public class LandingActivity extends AuthenticatedActivity
{
    private NavBanner navBanner;
    private HomeSubScene homeFragment;


    @Override
    protected void OnInitializeObjects()
    {
        homeFragment = new HomeSubScene();
    }

    @Override
    protected void OnInitializeViews()
    {
        setContentView(R.layout.activity_home);

        navBanner = findViewById(R.id.landing_nav_banner);
        navBanner.setOnNavItemSelected(frame -> {
            Toast.makeText(this, "Scroll To: " + frame, Toast.LENGTH_SHORT).show();
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.landing_subscenes_container, homeFragment)
                .commit();

        // This callback will only be called when the activity is at least started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed()
            {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.landing_subscenes_container);

                if (fragment instanceof OnFragmentBackPressedListener)
                    ((OnFragmentBackPressedListener) fragment).onBackPressed();
            }
        };

        // Add the callback to the back dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}