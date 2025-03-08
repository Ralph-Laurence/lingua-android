package psu.signlinguaasl.scene;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import psu.signlinguaasl.IActivityBlueprint;
import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.utils.Prefs;
import psu.signlinguaasl.ui.animations.FadeViewPagerPageTransformer;
import psu.signlinguaasl.ui.animations.Transitions;
import psu.signlinguaasl.ui.viewadapters.EntryActivityViewPagerAdapter;

public class EntryActivity extends AppCompatActivity implements IActivityBlueprint
{
    private ViewPager2 viewPager2;
    private TabLayout pagerIndicator;
    private RelativeLayout pagerWrapper;
    private ImageView splashPagerImage;
    private Button letsGoButton;
    private Button getStartedButton;
    private TextView txtBtnNextPager;
    private EntryActivityViewPagerAdapter adapter;
    private RelativeLayout splashOverlay;
    private Context m_globalContext;
    private Context m_thisContext;
    private int lastPage;
    private int[] splashPagerImages;
    private boolean isUserDragging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        m_globalContext = this.getApplicationContext();
        m_thisContext = EntryActivity.this;

        setContentView(R.layout.activity_entry);

        // Allow the splash screen to show 3secs ... then proceed
        new Handler().postDelayed(() -> {

            // We will skip the intro when the app is ran the 2nd time
            int lastRanStat = Prefs.getInstance(getApplicationContext())
                    .read(Prefs.PREF_KEY_FIRST_RUN, 0);

            if (lastRanStat == 1)
            {
                goToLoginPage();
                return;
            }

            InitializeObjects();
            InitializeViews();

        }, 3000);
    }

    @Override
    public void InitializeObjects()
    {
        splashPagerImages = new int[]
        {
            R.drawable.entry_screen_splash_0,
            R.drawable.entry_screen_splash_1,
            R.drawable.entry_screen_splash_2,
            R.drawable.entry_screen_splash_3,
            R.drawable.entry_screen_splash_4,
        };

        adapter = new EntryActivityViewPagerAdapter
        (
            m_thisContext,
            splashPagerImages.length
        );

        lastPage = splashPagerImages.length - 1;
    }

    @Override
    public void InitializeViews()
    {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        splashOverlay    = findViewById(R.id.entry_activity_splash_overlay);
        splashPagerImage = findViewById(R.id.entry_activity_splash_image);
        viewPager2       = findViewById(R.id.entry_activity_content_view_pager);
        pagerIndicator   = findViewById(R.id.entry_activity_view_pager_indicators);
        pagerWrapper     = findViewById(R.id.entry_activity_view_pager_indicator_wrapper);
        txtBtnNextPager  = findViewById(R.id.entry_activity_btntext_next);

        viewPager2.setAdapter(adapter);
        viewPager2.setPageTransformer(new FadeViewPagerPageTransformer());
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Your existing code here...

                // The "Let's go" button is located at the very first page.
                if (position == 0 && letsGoButton == null) {
                    RegisterLetsGoButtonClick(viewPager2);
                }

                if (position == lastPage && getStartedButton == null) {
                    RegisterGetStartedButtonClick();
                }

                SwitchPagerSplashImage(position);

                if (position == 0 || position == lastPage) {
                    pagerWrapper.setVisibility(View.INVISIBLE);
                    return;
                }

                pagerWrapper.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    isUserDragging = true;
                } else if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_SETTLING) {
                    isUserDragging = false;
                }
            }
        });

        // Set up TabLayout with ViewPager2
        new TabLayoutMediator(pagerIndicator, viewPager2, (tab, position) -> {
            // Optionally set the tab text here (e.g., tab.setText("Page " + (position + 1)));
        }).attach();

        // Iterate through each tab and disable clicks
        for (int i = 0; i < pagerIndicator.getTabCount(); i++) {
            View tab = ((ViewGroup) pagerIndicator.getChildAt(0)).getChildAt(i);
            tab.setEnabled(false);
        }

        txtBtnNextPager.setOnClickListener(view -> fakeDragToNextPage(viewPager2));

        Transitions.FadeOut(splashOverlay, 1000, () -> splashOverlay.setVisibility(View.GONE));
    }

    private void SwitchPagerSplashImage(int position)
    {
        // Decode the new bitmap
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), splashPagerImages[position]);

        // Set the new bitmap
        splashPagerImage.setImageBitmap(bmp);

        // Create a fade-in animation
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(splashPagerImage, "alpha", 0f, 1f);
        fadeIn.setDuration(750); // Set the duration of the animation
        fadeIn.start();
    }

    private void RegisterLetsGoButtonClick(ViewPager2 viewPager2)
    {
        EntryActivityViewPagerAdapter.ViewHolder inflatedView = adapter.inflatedView;

        if (inflatedView == null)
        {
            Log.i("console", "No inflated view");
            return;
        }

        letsGoButton = (Button) inflatedView.FindView(R.id.entry_activity_btn_lets_go);

        if (letsGoButton == null)
            return;

        letsGoButton.setOnClickListener(view -> {
            // When the button was clicked, scroll to the next item
            //  viewPager2.setCurrentItem(position + 1, true);
            fakeDragToNextPage(viewPager2);
        });
    }

    private void RegisterGetStartedButtonClick()
    {
        EntryActivityViewPagerAdapter.ViewHolder inflatedView = adapter.inflatedView;

        if (inflatedView == null)
        {
            Log.i("console", "No inflated view");
            return;
        }

        getStartedButton = (Button) inflatedView.FindView(R.id.entry_activity_btn_get_started);

        if (getStartedButton == null)
            return;

        getStartedButton.setOnClickListener(view -> goToLoginPage());
    }

    private void fakeDragToNextPage(ViewPager2 viewPager2)
    {
        // Temporarily disable user swipes
        viewPager2.setUserInputEnabled(false);

        if (viewPager2.isFakeDragging() || isUserDragging)
            return;

        final int DURATION = 500; // Duration of the fake drag in milliseconds
        final int PIXELS_PER_FRAME = 50; // Number of pixels to drag per frame

        // Start the fake drag if not already dragging and viewPager2 is not currently dragging
        viewPager2.beginFakeDrag();

        Handler handler = new Handler(Looper.getMainLooper());
        long startTime = SystemClock.uptimeMillis();

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - startTime;

                if (viewPager2.isFakeDragging() && elapsed < DURATION)
                {
                    // Compute the distance to drag based on the elapsed time and speed
                    float dragDistance = PIXELS_PER_FRAME * (elapsed / (float) DURATION);
                    viewPager2.fakeDragBy(-dragDistance);
                    handler.postDelayed(this, 16); // Post again for the next frame
                }
                else
                {
                    // End the fake drag if still dragging
                    if (viewPager2.isFakeDragging()) {
                        viewPager2.endFakeDrag();
                    }

                    //Enable user swipes
                    viewPager2.setUserInputEnabled(true);
                }
            }
        });
    }

    private void goToLoginPage()
    {
        Intent loginActivity = new Intent(m_thisContext, LoginActivity.class);
        loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginActivity);
        finish();
    }
}