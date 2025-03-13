package psu.signlinguaasl.ui.viewadapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.models.Tutor;
import psu.signlinguaasl.ui.viewholders.TutorViewHolder;

public class TutorAdapter extends RecyclerView.Adapter<TutorViewHolder>
{
    private List<Tutor> m_tutors;

    public TutorAdapter(List<Tutor> tutors)
    {
        m_tutors = tutors;
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tutor, parent, false);
        return new TutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int position)
    {
        Tutor tutor = m_tutors.get(position);

        holder.nameTextView.setText(tutor.getName());
        holder.learnersTextView.setText(String.valueOf(tutor.getTotalLearners()));
        holder.bioTextView.setText(String.valueOf(tutor.getBio()));
        holder.ratingsTextView.setText(String.valueOf(tutor.getRatings()));

        String imageUrl = tutor.getPhoto().replace("\\", "");
        Log.e("console", "Image URL: " + imageUrl);

        Glide.with(holder.photoImageView.getContext())
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(30, 0))
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

                .into(holder.photoImageView);
    }

    @Override
    public int getItemCount()
    {
        return m_tutors.size();
    }
}
