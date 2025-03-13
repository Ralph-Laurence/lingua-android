package psu.signlinguaasl.ui.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import psu.signlinguaasl.R;

public class TutorViewHolder extends RecyclerView.ViewHolder
{
    public TextView nameTextView;
    public TextView learnersTextView;
    public TextView bioTextView;
    public TextView ratingsTextView;
    public ImageView photoImageView;

    public TutorViewHolder(@NonNull View itemView)
    {
        super(itemView);

        nameTextView        = itemView.findViewById(R.id.list_item_tutor_name);
        learnersTextView    = itemView.findViewById(R.id.list_item_tutor_total_learners);
        bioTextView         = itemView.findViewById(R.id.list_item_tutor_bio);
        ratingsTextView     = itemView.findViewById(R.id.list_item_tutor_total_ratings);
        photoImageView      = itemView.findViewById(R.id.list_item_tutor_photo);
    }
}
