package psu.signlinguaasl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.models.User;
import psu.signlinguaasl.localservice.security.SecureAuthToken;
import psu.signlinguaasl.localservice.utils.Str;
import psu.signlinguaasl.scene.LoginActivity;

public abstract class AuthenticatedActivity
        extends AppCompatActivity
        implements IActivityBlueprint
{

    /**
     * Class object instantiation goes here, after super.onCreate has been called.
     */
    protected abstract void OnInitializeObjects();

    /**
     * Views initialization goes here, after object initializations.
     */
    protected abstract void OnInitializeViews();

    private AuthenticatedSession m_session;
    private Context m_context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        InitializeObjects();
        InitializeViews();
    }

    @Override
    public void InitializeObjects()
    {
        m_context = this.getApplicationContext();

        // Check if the user can access this activity.
        m_session = AuthenticatedSession.getInstance(this.getApplicationContext());

        if (Str.IsNullOrEmpty(m_session.getAuthToken()) || m_session.getUser() == null)
        {
            Intent loginIntent = new Intent(m_context, LoginActivity.class);
            startActivity(loginIntent);
            finish();

            return;
        }

        OnInitializeObjects();
    }

    @Override
    public void InitializeViews()
    {
        OnInitializeViews();

        // Extend content upto the status bar
        int noLimits = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        getWindow().setFlags(noLimits, noLimits);

        // Make status bar translucent
        // Window window = getWindow();
        // window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        // window.setStatusBarColor(Color.TRANSPARENT);

        // This ensures that all activities that extend this abstract activity will
        // correctly handle system window insets like the status bar, navigation bar,
        // or cutout areas (notches) on a device's screen.

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Helper function to get the authenticated user
    protected User user()
    {
        if (m_session == null)
            return null;

        return m_session.getUser();
    }
}