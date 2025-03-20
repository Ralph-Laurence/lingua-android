package psu.signlinguaasl.scene;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.controllers.TutorsController;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.ui.viewadapters.MyTutorCardViewAdapter;

public class MyTutorsActivity extends BaseTutorsListActivity
{
    private RecyclerView cardsContainer;
    private MyTutorCardViewAdapter adapter;

    @Override
    protected int useLayout()
    {
        return R.layout.activity_my_tutors;
    }

    @Override
    protected String useTitle()
    {
        return "My Tutors";
    }

    @Override
    protected int useFetchMode()
    {
        return TutorsController.FETCH_MODE_MY_TUTORS;
    }

    @Override
    protected void OnCreateViews()
    {
        super.OnCreateViews();

        cardsContainer = findViewById(R.id.activity_my_tutors_cardview_container);
        adapter = new MyTutorCardViewAdapter(this, getTutorList());

        cardsContainer.setLayoutManager(new GridLayoutManager(this, 2));
        cardsContainer.setAdapter(adapter);
    }

    @Override
    protected void onTutorsRetrieved(List<Tutor> tutors)
    {
        adapter.notifyDataSetChanged();
    }
}