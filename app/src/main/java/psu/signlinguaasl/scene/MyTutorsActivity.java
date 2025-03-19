package psu.signlinguaasl.scene;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import psu.signlinguaasl.R;
import psu.signlinguaasl.base.BaseAuthenticatedActivity;
import psu.signlinguaasl.localservice.controllers.MyTutorsController;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.ui.custom.Modal;
import psu.signlinguaasl.ui.viewadapters.MyTutorCardViewAdapter;

public class MyTutorsActivity extends BaseAuthenticatedActivity
{
    private RecyclerView cardsContainer;
    private MyTutorsController controller;
    private LayoutInflater layoutInflater;
    private Modal modal;

    @Override
    protected void Awake()
    {
        modal = new Modal(this);
        layoutInflater = LayoutInflater.from(this);

        controller = new MyTutorsController(this);
        controller.onFetchBegan = () -> modal.ShowLoading("Fetching records, please wait...");
        controller.onRetrieveFailed = (msg) -> {
            modal.closeLoading();
            modal.ShowDanger(msg);
        };
        controller.onTutorsRetrieved = tutors -> {
            modal.closeLoading();
            Populate(tutors);
        };
        controller.fetchTutorsList();
    }

    @Override
    protected int useLayout()
    {
        return R.layout.activity_my_tutors;
    }

    @Override
    protected void OnCreateViews()
    {
        ((TextView) findViewById(R.id.merge_tutors_list_header_title)).setText("My Tutors");
        cardsContainer = findViewById(R.id.activity_my_tutors_cardview_container);
    }

    @Override
    public void OnBackPressed()
    {
        // Finish the current activity and don't keep it in the back stack
        finish();
    }

    private void Populate(List<Tutor> tutors)
    {
        cardsContainer.setLayoutManager(new GridLayoutManager(this, 2));
        MyTutorCardViewAdapter adapter = new MyTutorCardViewAdapter(this, tutors);

        cardsContainer.setAdapter(adapter);
//        for (Tutor tutor : tutors)
//        {
//            // Create a new CardView
//            View cardView = layoutInflater.inflate(R.layout.list_item_my_tutors_cardview, null);
//
//            ((TextView) cardView.findViewById(R.id.tutor_carditem_tutor_name)).setText(tutor.getName());
//            ImageView profilePicture = cardView.findViewById(R.id.tutor_carditem_profile_photo);
//
//            String imageUrl = tutor.getPhoto().replace("\\", "");
//
//            Glide.with(profilePicture.getContext())
//                    .load(imageUrl)
//                    .transform(new RoundedCornersTransformation(30, 0))
//                    .placeholder(R.drawable.default_avatar)
//                    .addListener(new RequestListener<>()
//                    {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
//                        {
//                            // Handle the error here
//                            Log.e("Glide", "Image load failed", e);
//                            return false; // Important to return false so the error placeholder can be placed
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource)
//                        {
//                            // Handle the success here
//                            Log.d("Glide", "Image loaded successfully");
//                            return false; // Important to return false so the resource can be displayed
//                        }
//                    })
//                    .into(profilePicture);
//
//            // Add the CardView to the parent layout
//            cardsContainer.addView(cardView);
//       }
    }
}