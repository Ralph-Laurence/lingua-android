package psu.signlinguaasl.ui.viewadapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.models.Disabilities;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.localservice.utils.Convert;
import psu.signlinguaasl.ui.viewholders.MyTutorCardViewHolder;

public class MyTutorCardViewAdapter extends RecyclerView.Adapter<MyTutorCardViewHolder>
{
    private final List<Tutor> tutors;
    private final Context m_context;

    public MyTutorCardViewAdapter(Context context, List<Tutor> tutors)
    {
        this.m_context = context;
        this.tutors = tutors;
    }

    @NonNull
    @Override
    public MyTutorCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Inflate the reusable CardView
        View view = LayoutInflater.from(m_context).inflate(R.layout.list_item_my_tutors_cardview, parent, false);
        return new MyTutorCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTutorCardViewHolder holder, int position)
    {
        Tutor tutor = tutors.get(position);

        // Set the name
        holder.tutorName.setText(tutor.getName());
        holder.totalRatings.setText(String.valueOf(tutor.getRatings()));
        holder.totalLearners.setText(String.valueOf(tutor.getTotalLearners()));

        if (tutor.getDisability() == Disabilities.NO_DISABILITY)
        {
            holder.disabilityBadge.setVisibility(View.INVISIBLE);
        }
        else
        {
            Drawable disabilityBadge = Disabilities.getInstance(m_context)
                    .mapBadgeIcon(m_context, tutor.getDisability());

            holder.disabilityBadge.setImageDrawable(disabilityBadge);
            holder.disabilityBadge.setVisibility(View.VISIBLE);
        }

        // Set a fixed height for the ImageView (150dp)
        int heightInPixels = Convert.DpToPx(m_context, 150);

        ViewGroup.LayoutParams layoutParams = holder.profilePicture.getLayoutParams();
        layoutParams.height = heightInPixels;
        holder.profilePicture.setLayoutParams(layoutParams);

        // Load the profile picture using Glide
        Glide.with(m_context)
                .load(tutor.getPhoto().replace("\\", ""))
                .placeholder(R.drawable.default_avatar)
                .fitCenter()  // Use fitCenter to maintain aspect ratio
                .transform(new RoundedCornersTransformation(15, 0))
                .into(holder.profilePicture);
    }

    @Override
    public int getItemCount() {
        return tutors.size();
    }
}