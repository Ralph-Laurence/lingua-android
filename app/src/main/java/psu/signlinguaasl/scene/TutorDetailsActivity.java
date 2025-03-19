package psu.signlinguaasl.scene;

import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import psu.signlinguaasl.R;
import psu.signlinguaasl.base.BaseAuthenticatedActivity;
import psu.signlinguaasl.localservice.controllers.TutorDetailsController;
import psu.signlinguaasl.localservice.models.Disabilities;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.localservice.utils.Str;
import psu.signlinguaasl.ui.animations.LinearAnimations;
import psu.signlinguaasl.ui.animations.Transitions;
import psu.signlinguaasl.ui.custom.Modal;
import psu.signlinguaasl.ui.viewadapters.DocumentaryProofAdapter;

public class TutorDetailsActivity extends BaseAuthenticatedActivity
{
    private Modal modal;
    private String tutorId;

    private TutorDetailsController controller;
    private LinearLayout loadingMaskOverlay;

    private TextView txv_tutorName;
    private TextView txv_tutorBio;
    private TextView txv_tutorTotalRatings;
    private TextView txv_tutorTotalReviews;
    private TextView txv_tutorTotalLearners;
    private TextView txv_tutorAddress;
    private TextView txv_tutorContact;
    private TextView txv_tutorEmail;
    private TextView txv_tutorAbout;
    private TextView resumeSectionHeader;
    private Button btnShowMoreAbout;
    private RecyclerView educProofsRecyclerView;
    private RecyclerView workProofsRecyclerView;
    private RecyclerView certProofsRecyclerView;
    private ImageView profilePictureImageView;
    private ImageView disabilityIcon;
    private LinearLayout disabilityBadge;
    private LinearLayout certificationsContainer;
    private LinearLayout educationContainer;
    private LinearLayout workexpContainer;
    private static final int ABOUT_ME_MAX_VISIBLE_LINES = 5;

    @Override
    protected void Awake()
    {
        controller = new TutorDetailsController(this);
        tutorId = getIntent().getStringExtra("tutorId");
        modal   = new Modal(this);
    }

    @Override
    protected int useLayout()
    {
        return R.layout.activity_tutor_details;
    }

    @Override
    protected void OnCreateViews()
    {
        ImageView loadingGearLarge  = findViewById(R.id.activity_tutor_details_gear_large);
        ImageView loadingGearMedium = findViewById(R.id.activity_tutor_details_gear_med);
        profilePictureImageView = findViewById(R.id.activity_tutor_details_profile_photo);

        loadingMaskOverlay      = findViewById(R.id.activity_tutor_details_loading_mask);

        txv_tutorName           = findViewById(R.id.activity_tutor_details_tutor_name);
        txv_tutorContact        = findViewById(R.id.activity_tutor_details_phone);
        txv_tutorAddress        = findViewById(R.id.activity_tutor_details_address);
        txv_tutorTotalReviews   = findViewById(R.id.activity_tutor_details_total_reviews);
        txv_tutorTotalRatings   = findViewById(R.id.activity_tutor_details_total_ratings);
        txv_tutorBio            = findViewById(R.id.activity_tutor_details_bio);
        txv_tutorTotalLearners  = findViewById(R.id.activity_tutor_details_total_learners);
        txv_tutorEmail          = findViewById(R.id.activity_tutor_details_email);
        txv_tutorAbout          = findViewById(R.id.activity_tutor_details_about_me);

        btnShowMoreAbout        = findViewById(R.id.activity_tutor_details_showmore_aboutme);

        educProofsRecyclerView  = findViewById(R.id.activity_tutor_details_educ_proofs);
        workProofsRecyclerView  = findViewById(R.id.activity_tutor_details_work_proofs);
        certProofsRecyclerView  = findViewById(R.id.activity_tutor_details_cert_proofs);

        disabilityBadge         = findViewById(R.id.activity_tutor_details_disability_badge);
        disabilityIcon          = findViewById(R.id.activity_tutor_details_disability_icn);

        certificationsContainer = findViewById(R.id.activity_tutor_details_certifications_container);
        workexpContainer        = findViewById(R.id.activity_tutor_details_workexp_container);
        educationContainer      = findViewById(R.id.activity_tutor_details_education_container);

        resumeSectionHeader     = findViewById(R.id.activity_tutor_details_resume_section_header);

        DisableRecyclerViewScroll(educProofsRecyclerView);
        DisableRecyclerViewScroll(workProofsRecyclerView);
        DisableRecyclerViewScroll(certProofsRecyclerView);

        Button btnBack = findViewById(R.id.activity_tutor_details_btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Toggle TextView expansion on button click
        btnShowMoreAbout.setOnClickListener(new View.OnClickListener()
        {
            private boolean isExpanded = false;

            @Override
            public void onClick(View v)
            {
                int showMoreArrow = isExpanded ? R.drawable.baseline_arrow_drop_down_24 : R.drawable.baseline_arrow_drop_up_24;
                LinearAnimations.animateTextViewExpansion(txv_tutorAbout, ABOUT_ME_MAX_VISIBLE_LINES, !isExpanded);
                btnShowMoreAbout.setText(isExpanded ? "Show More" : "Show Less");
                btnShowMoreAbout.setCompoundDrawablesWithIntrinsicBounds
                (
                    null,
                    null,
                    ContextCompat.getDrawable(TutorDetailsActivity.this, showMoreArrow),
                    null
                );
                isExpanded = !isExpanded;
            }
        });

        LinearAnimations.SpinRight(loadingGearLarge, 4.0f);
        LinearAnimations.SpinLeft(loadingGearMedium, 2.0f);
    }

    @Override
    protected void Begin()
    {
        controller.fetch(tutorId, (tutor) -> {
            presentTutorDetails(tutor);
            Transitions.FadeOut(loadingMaskOverlay, 500, null);
        },
       (err) -> modal.ShowDanger(err, "Failure", this::finish));
    }

    private void presentTutorDetails(Tutor tutor)
    {
        try
        {
            txv_tutorName.setText(tutor.getName());
            txv_tutorContact.setText(tutor.getContact());
            txv_tutorAddress.setText(tutor.getAddress());
            txv_tutorEmail.setText(tutor.getEmail());
            txv_tutorTotalReviews.setText(String.valueOf(tutor.getReviews()));
            txv_tutorTotalRatings.setText(String.valueOf(tutor.getRatings()));
            txv_tutorTotalLearners.setText(String.valueOf(tutor.getTotalLearners()));

            if (!Str.IsNullOrEmpty(tutor.getBio()))
                txv_tutorBio.setText(tutor.getBio());

            String aboutMeRaw = Str.sanitize(tutor.getAboutMe());
            Spanned aboutMe = HtmlCompat.fromHtml(aboutMeRaw, HtmlCompat.FROM_HTML_MODE_LEGACY);

            txv_tutorAbout.setText(aboutMe);
            txv_tutorAbout.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

            // Only show the "Show More" button under the "About Me"
            // when the actual content exceeds the max line height
            // Wait for the TextView to finish layout to accurately calculate line count
            txv_tutorAbout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout() {
                    // Remove the listener to prevent multiple callbacks
                    txv_tutorAbout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // Check if the content exceeds the max lines
                    txv_tutorAbout.setMaxLines(Integer.MAX_VALUE); // Temporarily allow full expansion
                    txv_tutorAbout.measure(
                            View.MeasureSpec.makeMeasureSpec(txv_tutorAbout.getWidth(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    );

                    int fullContentLineCount = txv_tutorAbout.getLineCount();
                    txv_tutorAbout.setMaxLines(ABOUT_ME_MAX_VISIBLE_LINES); // Restore ellipsized state

                    if (fullContentLineCount > ABOUT_ME_MAX_VISIBLE_LINES) {
                        btnShowMoreAbout.setVisibility(View.VISIBLE); // Show the "Show More" button
                    } else {
                        btnShowMoreAbout.setVisibility(View.GONE); // Hide it otherwise
                    }
                }
            });

            int docProofCounts = 0;

            // Show the documentary proofs
            if (tutor.getEducation() != null && !tutor.getEducation().isEmpty())
            {
                DocumentaryProofAdapter adapter = new DocumentaryProofAdapter(tutor.getEducation());
                educProofsRecyclerView.setAdapter(adapter);
                docProofCounts++;
            }
            else
            {
                Hide(educationContainer);
            }

            if (tutor.getWorkExperience() != null && !tutor.getWorkExperience().isEmpty())
            {
                DocumentaryProofAdapter adapter = new DocumentaryProofAdapter(tutor.getWorkExperience());
                workProofsRecyclerView.setAdapter(adapter);
                docProofCounts++;
            }
            else
            {
                Hide(workexpContainer);
            }

            if (tutor.getCertifications() != null && !tutor.getCertifications().isEmpty())
            {
                DocumentaryProofAdapter adapter = new DocumentaryProofAdapter(tutor.getCertifications());
                certProofsRecyclerView.setAdapter(adapter);
                docProofCounts++;
            }
            else
            {
                Hide(certificationsContainer);
            }

            // Hide the section header "Resume" if there weren't documentary proofs
            if (docProofCounts == 0)
            {
                Hide(resumeSectionHeader);
            }

            // Load the profile picture
            String imageUrl = tutor.getPhoto().replace("\\", "");

            Glide.with(profilePictureImageView.getContext())
                    .load(imageUrl)
                    .transform(new CenterCrop(), new RoundedCornersTransformation(45, 0))
                    .addListener(new RequestListener<>()
                    {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                        {
                            // Handle the error here
                            Log.e("Glide", "Image load failed", e);
                            return false; // Important to return false so the error placeholder can be placed
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource)
                        {
                            // Handle the success here
                            Log.d("Glide", "Image loaded successfully");
                            return false; // Important to return false so the resource can be displayed
                        }
                    })
                    .into(profilePictureImageView);

            // Show the disability badge depending on the tutor's current disability
            if (tutor.getDisability() > Disabilities.NO_DISABILITY)
            {
                Drawable disabilityIcn = Disabilities.getInstance(this).mapBadgeIcon(this, tutor.getDisability());
                // Show the disability badge
                disabilityIcon.setImageDrawable(disabilityIcn);
                Show(disabilityBadge);
            }
            else
            {
                // Hide the disability badge
                Hide(disabilityBadge);
            }
        }
        catch (Exception ex)
        {
            modal.ShowDanger("Aww, this shouldn't happen. Something is broken on our end. Don't worry, it's not your fault.", "Application Error", this::finish);
        }
    }

    private void DisableRecyclerViewScroll(RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false; // Disable scrolling
            }
        });

        recyclerView.setNestedScrollingEnabled(false);
    }
}