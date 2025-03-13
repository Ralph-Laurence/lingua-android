package psu.signlinguaasl.scene;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import psu.signlinguaasl.AuthenticatedActivity;
import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.controllers.FindTutorsController;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.ui.custom.Modal;
import psu.signlinguaasl.ui.viewadapters.TutorAdapter;

public class FindTutorsActivity extends AuthenticatedActivity
{
    private RecyclerView recyclerView;
    private TutorAdapter tutorAdapter;
    private List<Tutor> tutorList;

    private FindTutorsController findTutorsController;
    private Modal modal;

    @Override
    protected void OnInitializeObjects()
    {
        modal = new Modal(this);
        tutorList = new ArrayList<>();
        tutorAdapter = new TutorAdapter(tutorList);

        findTutorsController = new FindTutorsController(this);
        findTutorsController.onTutorsRetrieved = tutors -> {
            tutorList.addAll(tutors);
            tutorAdapter.notifyDataSetChanged();
        };

        findTutorsController.onRetrieveFailed = (msg) -> modal.ShowDanger(msg);
    }

    @Override
    protected void OnInitializeViews()
    {
        setContentView(R.layout.activity_tutors_list);
        recyclerView = findViewById(R.id.find_tutors_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tutorAdapter);

        findTutorsController.fetchTutorsList();

        // Implement pagination/lazy loading
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == tutorList.size() - 1)
                {
                    findTutorsController.fetchTutorsList();
                }
            }
        });
    }
}