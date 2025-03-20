package psu.signlinguaasl.scene;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.controllers.TutorsController;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.ui.viewadapters.TutorAdapter;

public class FindTutorsActivity extends BaseTutorsListActivity
{
    private RecyclerView recyclerView;
    private TutorAdapter tutorAdapter;

    @Override
    protected int useLayout() {
        return R.layout.activity_find_tutors; // Specify your layout resource directly here
    }

    @Override
    protected String useTitle()
    {
        return "Find Tutors";
    }

    @Override
    protected int useFetchMode()
    {
        return TutorsController.FETCH_MODE_FIND_TUTORS;
    }

    @Override
    protected void Awake()
    {
        super.Awake();

        tutorAdapter = new TutorAdapter(getTutorList(), position -> {
            Tutor selectedTutor = getTutorList().get(position);

            if (selectedTutor != null)
            {
                String tutorId = selectedTutor.getTutorId();
                showTutorDetails(tutorId);
            }
        });
    }

    @Override
    protected void OnCreateViews()
    {
        super.OnCreateViews();

        recyclerView = findViewById(R.id.find_tutors_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tutorAdapter);
    }

    @Override
    protected void onTutorsRetrieved(List<Tutor> tutors)
    {
        tutorAdapter.notifyDataSetChanged();
    }
}