package psu.signlinguaasl.scene;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import psu.signlinguaasl.R;
import psu.signlinguaasl.base.BaseAuthenticatedActivity;
import psu.signlinguaasl.localservice.controllers.FindTutorsController;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.localservice.utils.Str;
import psu.signlinguaasl.ui.custom.Modal;
import psu.signlinguaasl.ui.viewadapters.TutorAdapter;

public class FindTutorsActivity extends BaseAuthenticatedActivity
{
    private RecyclerView recyclerView;
    private TutorAdapter tutorAdapter;
    private List<Tutor> tutorList;

    private FindTutorsController findTutorsController;
    private Modal modal;
    private ImageButton btnSearchTutors;
    private EditText inputSearchTerm;
    private LinearLayout paginationContainer;
    private LinearLayout searchParamsContainer;
    private TextView searchTermLabel;
    private TextView searchResultsCounter;
    private Button btnClearSearch;

    @Override
    protected void Awake()
    {
        modal        = new Modal(this);
        tutorList    = new ArrayList<>();
        tutorAdapter = new TutorAdapter(tutorList, position -> {
            Tutor selectedTutor = tutorList.get(position);

            if (selectedTutor != null)
            {
                String tutorId = selectedTutor.getTutorId();
                showTutorDetails(tutorId);
            }
        });

        findTutorsController = new FindTutorsController(this);

        findTutorsController.onFetchBegan = () -> modal.ShowLoading("Fetching records, please wait...");

        findTutorsController.onTutorsRetrieved = tutors -> {
            modal.closeLoading();
            tutorList.clear();
            tutorList.addAll(tutors);
            tutorAdapter.notifyDataSetChanged();

            if (!Str.IsNullOrEmpty(getSearchTerm()))
            {
                String searchTermResult = String.format("Showing search results for \"%s\"", getSearchTerm());
                searchTermLabel.setText(searchTermResult);

                String strResultsPlural  = tutorList.size() == 1 ? "result" : "results";
                String searchResultTotal = String.format("%d %s", tutorList.size(), strResultsPlural);
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

        findTutorsController.onRetrieveFailed = (msg) -> {
            modal.closeLoading();
            modal.ShowDanger(msg);
        };
    }

    @Override
    protected int useLayout()
    {
        return R.layout.activity_find_tutors;
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
        ((TextView) findViewById(R.id.merge_tutors_list_header_title)).setText("Find Tutors");

        recyclerView = findViewById(R.id.find_tutors_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tutorAdapter);

        inputSearchTerm = findViewById(R.id.merge_tutors_list_header_input_search);
        findTutorsController.fetchTutorsList();

        searchParamsContainer = findViewById(R.id.container_search_parameters);
        searchResultsCounter  = findViewById(R.id.txv_search_total_results);

        searchTermLabel = findViewById(R.id.txv_search_term);
        btnSearchTutors = findViewById(R.id.merge_tutors_list_header_btn_search);
        btnSearchTutors.setOnClickListener(v -> {

            if (Str.IsNullOrEmpty(getSearchTerm()))
            {
                searchParamsContainer.setVisibility(View.GONE);
                modal.ShowWarn("Please enter a search term such as the tutor's firstname or lastname.");
            }
            else
            {
                findTutorsController.fetchTutorsList(getSearchTerm());
            }
        });

        btnClearSearch  = findViewById(R.id.btn_clear_find_tutors);
        btnClearSearch.setOnClickListener(v -> {
            inputSearchTerm.setText("");
            findTutorsController.fetchTutorsList();
        });

        paginationContainer = findViewById(R.id.pagination_container);
    }

    // Business Logic

    private String getSearchTerm() {
        return inputSearchTerm.getText().toString();
    }

    private void showTutorDetails(String tutorId)
    {
        if (Str.IsNullOrEmpty(tutorId))
        {
            modal.ShowDanger("Sorry, we're having trouble fetching the tutor's details. Please try again later.");
            return;
        }

        Intent intent = new Intent(FindTutorsActivity.this, TutorDetailsActivity.class);
        intent.putExtra("tutorId", tutorId);
        startActivity(intent);
    }
}