package psu.signlinguaasl.scene;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import psu.signlinguaasl.R;
import psu.signlinguaasl.base.BaseAuthenticatedActivity;
import psu.signlinguaasl.localservice.controllers.TutorsController;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.localservice.utils.Str;
import psu.signlinguaasl.ui.custom.Modal;

@SuppressLint("DefaultLocale")
public abstract class BaseTutorsListActivity extends BaseAuthenticatedActivity
{
    private Modal modal;
    private List<Tutor> tutorList;
    private ImageButton btnSearchTutors;
    private EditText inputSearchTerm;
    private LinearLayout paginationContainer;
    private LinearLayout searchParamsContainer;
    private TextView searchTermLabel;
    private TextView searchResultsCounter;
    private Button btnClearSearch;
    protected TutorsController controller;

    protected abstract void onTutorsRetrieved(List<Tutor> tutors);
    protected abstract String useTitle();
    protected abstract int useFetchMode();

    @Override
    protected void Awake()
    {
        modal      = new Modal(this);
        tutorList  = new ArrayList<>();
        controller = new TutorsController(this);
    }

    @Override
    public void OnBackPressed()
    {
        // Finish the current activity and don't keep it in the back stack
        finish();
    }

    @Override
    protected void OnCreateViews()
    {
        ((TextView) findViewById(R.id.merge_tutors_list_header_title)).setText(useTitle());

        paginationContainer     = findViewById(R.id.pagination_container);
        searchParamsContainer   = findViewById(R.id.container_search_parameters);
        searchResultsCounter    = findViewById(R.id.txv_search_total_results);
        inputSearchTerm         = findViewById(R.id.merge_tutors_list_header_input_search);
        searchTermLabel         = findViewById(R.id.txv_search_term);
        btnSearchTutors         = findViewById(R.id.merge_tutors_list_header_btn_search);
        btnClearSearch          = findViewById(R.id.btn_clear_find_tutors);

        btnSearchTutors.setOnClickListener(v -> {

            if (Str.IsNullOrEmpty(getSearchTerm()))
            {
                searchParamsContainer.setVisibility(View.GONE);
                modal.ShowWarn("Please enter a search term such as the tutor's firstname or lastname.");
                return;
            }

            controller.fetchTutors(useFetchMode(), getSearchTerm());
        });

        btnClearSearch.setOnClickListener(v -> {
            inputSearchTerm.setText("");
            controller.fetchTutors(useFetchMode());
        });
    }

    //
    //==================================
    //--------- Business Logic ---------
    //==================================
    //

    /**
     * Perform some necessary initializations after the views
     * are created and the activity is ready
     */
    @Override
    protected void Begin()
    {
        controller.onFetchBegan = () -> modal.ShowLoading("Fetching records, please wait...");
        controller.onTutorsRetrieved = tutors -> {
            modal.closeLoading();
            tutorList.clear();
            tutorList.addAll(tutors);

            onTutorsRetrieved(tutors);

            if (!Str.IsNullOrEmpty(getSearchTerm()))
            {
                String searchTermResult = String.format("Showing search results for \"%s\"", getSearchTerm());
                searchTermLabel.setText(searchTermResult);

                String searchResultTotal = String.format
                (
                    "%d %s",
                    tutorList.size(),
                    Str.pluralize("result", tutorList.size())
                );

                searchResultsCounter.setText(searchResultTotal);
                searchParamsContainer.setVisibility(View.VISIBLE);
                btnSearchTutors.setEnabled(false);
                inputSearchTerm.setEnabled(false);
            }
            else
            {
                searchParamsContainer.setVisibility(View.GONE);
                btnSearchTutors.setEnabled(true);
                inputSearchTerm.setEnabled(true);
            }
        };
        controller.onRetrieveFailed = (msg) -> {
            modal.closeLoading();
            modal.ShowDanger(msg);
        };

        // Immediately fetch the tutors when the activity loads
        controller.fetchTutors(useFetchMode());
    }

    /**
     * The list of tutors as adapter data source.
     */
    protected List<Tutor> getTutorList()
    {
        return tutorList;
    }

    /**
     * Get the entered search term from the input
     * @return The entered search term
     */
    private String getSearchTerm()
    {
        if (inputSearchTerm == null)
            return null;

        return inputSearchTerm.getText().toString();
    }

    /**
     * Launch an intent to show the tutor's details
     * @param tutorId - The hashed tutor id
     */
    protected void showTutorDetails(String tutorId)
    {
        if (Str.IsNullOrEmpty(tutorId))
        {
            modal.ShowDanger("Sorry, we're having trouble fetching the tutor's details. Please try again later.");
            return;
        }

        Intent intent = new Intent(this, TutorDetailsActivity.class);
        intent.putExtra("tutorId", tutorId);
        startActivity(intent);
    }
}
