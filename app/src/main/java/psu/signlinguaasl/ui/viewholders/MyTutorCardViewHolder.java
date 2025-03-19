package psu.signlinguaasl.ui.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import psu.signlinguaasl.R;

// ViewHolder to hold the references to the CardView's views
public class MyTutorCardViewHolder extends RecyclerView.ViewHolder
{
    public TextView tutorName;
    public TextView totalRatings;
    public TextView totalLearners;
    public ImageView profilePicture;
    public ImageView disabilityBadge;

    public MyTutorCardViewHolder(@NonNull View itemView)
    {
        super(itemView);
        tutorName       = itemView.findViewById(R.id.tutor_carditem_tutor_name);
        profilePicture  = itemView.findViewById(R.id.tutor_carditem_profile_photo);
        totalRatings    = itemView.findViewById(R.id.tutor_list_item_kpi_total_ratings);
        totalLearners   = itemView.findViewById(R.id.tutor_list_item_kpi_total_learners);
        disabilityBadge = itemView.findViewById(R.id.tutor_list_item_kpi_disability);
    }
}
