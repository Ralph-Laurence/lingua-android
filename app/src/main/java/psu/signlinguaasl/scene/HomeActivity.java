package psu.signlinguaasl.scene;

import android.widget.TextView;

import psu.signlinguaasl.AuthenticatedActivity;
import psu.signlinguaasl.R;

public class HomeActivity extends AuthenticatedActivity
{
    private TextView welcomeText;

    @Override
    protected void OnInitializeObjects()
    {

    }

    @Override
    protected void OnInitializeViews()
    {
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.home_welcome_text);
        String message = String.format("Welcome home, %s ... Glad to be back?", user().getFirstname());
        welcomeText.setText(message);
    }
}