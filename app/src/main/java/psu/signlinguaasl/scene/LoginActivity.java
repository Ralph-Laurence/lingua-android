package psu.signlinguaasl.scene;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import psu.signlinguaasl.IActivityBlueprint;
import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.controllers.LoginController;
import psu.signlinguaasl.localservice.models.Credentials;
import psu.signlinguaasl.localservice.utils.Prefs;
import psu.signlinguaasl.localservice.utils.Str;
import psu.signlinguaasl.ui.custom.Modal;

public class LoginActivity extends AppCompatActivity implements IActivityBlueprint
{
    private Button btnLogin;
    private EditText inputUmail;
    private EditText inputPassword;
    private CheckBox chkRememberMe;

    private LoginController loginController;
    private Modal modal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        InitializeObjects();
        InitializeViews();
    }

    public void InitializeObjects()
    {
        Prefs.getInstance(getApplicationContext()).store(Prefs.PREF_KEY_FIRST_RUN, 1);

        loginController = new LoginController(LoginActivity.this);
    }

    public void InitializeViews()
    {
//        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//        getWindow().getDecorView().setSystemUiVisibility(flags);

        // setContentView(R.layout.activity_login);

        // Set system UI flags for fullscreen layout
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
//        {
//            WindowInsetsController windowInsetsController = getWindow().getInsetsController();
//
//            if (windowInsetsController != null)
//            {
//                int behaviour = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE;
//                windowInsetsController.hide(WindowInsets.Type.statusBars());
//                windowInsetsController.setSystemBarsBehavior(behaviour);
//            }
//        }
//        else
//        {
//            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            getWindow().getDecorView().setSystemUiVisibility(flags);
//        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_screen_root), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//
//            // Only apply these to non-Pie
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
//                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//
//            return insets;
//        });

//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_login);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_screen_root), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_screen_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Make status bar transparent
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // For Android 12+ (API 31+), use the new WindowInsetsController
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // This is the modern way to control system bar appearance
            WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
            // Setting to false makes the status bar icons light (white)
            insetsController.setAppearanceLightStatusBars(false);

            // This enables edge-to-edge properly for Android 12+
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        }
        // For Android 10-11
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Uses the older setSystemUiVisibility approach
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        }
        // For Android 9 and below
        else
        {
            getWindow().getDecorView().setSystemUiVisibility
            (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );

            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        }

        inputPassword = findViewById(R.id.input_password);
        inputUmail = findViewById(R.id.input_umail);
        chkRememberMe = findViewById(R.id.chk_remember_me);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(view -> loginOnBtnClicked());

        modal = new Modal(this);

        // Check for cached user session data then perform auto login
        checkAuth();
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on)
    {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void loginOnBtnClicked()
    {
        String password = inputPassword.getText().toString();
        String umail = inputUmail.getText().toString();
        boolean rememberMe = chkRememberMe.isChecked();

        performLogin(new Credentials(umail, password), rememberMe);
    }

    private void checkAuth()
    {
        // Check if there was a cached user credential
        AuthenticatedSession authSession = AuthenticatedSession.getInstance(getApplicationContext());

        if (!authSession.checkSession())
            return;

        Credentials credentials = authSession.loadUserCredentials();

        // Auto login the user using his credentials
        if (credentials == null)
            return;

        // Just in case the internet is down, but there were last saved
        // credentials, it safe to assume we set the "remember me" option.
        if (!Str.IsNullOrEmpty(credentials.getUmail()) && !Str.IsNullOrEmpty(credentials.getPassword()))
            chkRememberMe.setChecked(true);

        performLogin(credentials, true);
    }

    private void performLogin(Credentials credentials, boolean rememberMe)
    {
        String password = credentials.getPassword();
        String umail = credentials.getUmail();

        loginController.InitiateLogin(umail, password, rememberMe,
                () -> {
                    // login began

                    modal.ShowLoading("Please wait while we log you in.");

                    btnLogin.setEnabled(false);
                    inputUmail.setEnabled(false);
                    inputPassword.setEnabled(false);
                },
                () -> {
                    // login end

                    modal.closeLoading();

                    btnLogin.setEnabled(true);
                    inputUmail.setEnabled(true);
                    inputPassword.setEnabled(true);
                });
    }
}