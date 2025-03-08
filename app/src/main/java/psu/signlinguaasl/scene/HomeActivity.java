package psu.signlinguaasl.scene;

import android.widget.TextView;

import psu.signlinguaasl.AuthenticatedActivity;
import psu.signlinguaasl.R;
import psu.signlinguaasl.base.BaseAuthenticatedActivity;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;

public class HomeActivity extends BaseAuthenticatedActivity
{
    private TextView welcomeText;

    @Override
    protected void OnAwake()
    {

    }

    @Override
    protected int useLayout()
    {
        return R.layout.activity_home;
    }

    @Override
    protected void OnCreateViews()
    {
        welcomeText = findViewById(R.id.home_welcome_text);

        AuthenticatedSession session = AuthenticatedSession.getInstance(this);

        String msg = String.format("Welcome home, %s ... Glad to be back?", session.getUser().getFirstname());
        welcomeText.setText(msg);
    }
}