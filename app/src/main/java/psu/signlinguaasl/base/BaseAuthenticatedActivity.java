package psu.signlinguaasl.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import psu.signlinguaasl.IActivityBlueprint;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.models.User;
import psu.signlinguaasl.scene.LoginActivity;

public abstract class BaseAuthenticatedActivity
        extends AppCompatActivity
        implements IActivityBlueprint
{

    private boolean isActivityInForeground = false;

    /**
     * Class object instantiation goes here, after super.onCreate has been called.
     */
    protected abstract void Awake();

    /**
     * Define the layout that the activity should use, from inheritor.
     * */
    protected abstract int useLayout();

    /**
     * Views initialization goes here, after object initializations.
     */
    protected abstract void OnCreateViews();

    /**
     * Called after all initializations had completed.
     */
    protected void Begin()
    {
        // This isn't strictly required to override
    }

    /*
    * Handle the back button press
    * */
    protected void OnBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        checkSessionState();

        InitializeObjects();
        InitializeViews();

        Begin();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        isActivityInForeground = true;
        checkSessionState();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        isActivityInForeground = false;

        // Check if the app is in the background and user has cleared it from the recent tasks list
        new Handler().postDelayed(() ->
        {
            if (isActivityInForeground)
                return;

            if (MyApp.getInstance() != null && !MyApp.getInstance().isAppInForeground())
            {
                // Clear session if the app is not in the foreground
                AuthenticatedSession.getInstance(getApplicationContext()).clearSession();
            }
        }, 500);
    }

    @Override
    public void InitializeObjects()
    {
        Awake();

        // This callback will only be called when the activity is at least started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed()
            {
                OnBackPressed();
            }
        };

        // Add the callback to the back dispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void InitializeViews()
    {
        EdgeToEdge.enable(this);

        // Extend content up to the status bar
        int noLimits = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        getWindow().setFlags(noLimits, noLimits);

        // Set system UI flags for fullscreen layout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            WindowInsetsController windowInsetsController = getWindow().getInsetsController();

            if (windowInsetsController != null)
            {
                int behaviour = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;
                windowInsetsController.hide(WindowInsets.Type.statusBars());
                windowInsetsController.setSystemBarsBehavior(behaviour);
            }
        }
        else
        {
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }

        setContentView(useLayout());

        // Retrieve the root view of the current layout
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content).getRootView();

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Only apply these to non-Pie
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        OnCreateViews();
    }

    private void checkSessionState()
    {
        User user = AuthenticatedSession.getInstance(getApplicationContext()).getUser();
        String token = AuthenticatedSession.getInstance(getApplicationContext()).getAuthToken();

        if (user == null || token == null || token.isEmpty()) {
            // Redirect to login screen if the session is invalid or empty
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    // Utility Methods
    protected void Show(View view)
    {
        if (view != null)
            view.setVisibility(View.VISIBLE);
    }

    protected void Hide(View view)
    {
        hideView(view, true);
    }

    protected void Hide(View view, boolean fullyHide)
    {
        hideView(view, fullyHide);
    }

    private void hideView(View view, boolean fullyHide)
    {
        if (view == null)
            return;

        if (fullyHide)
        {
            view.setVisibility(View.GONE);
            return;
        }

        view.setVisibility(View.INVISIBLE);
    }
}
