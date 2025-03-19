package psu.signlinguaasl.ui.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.interfaces.OnTutorItemClickListener;

public class TutorViewHolder extends RecyclerView.ViewHolder
{
    public TextView nameTextView;
    public TextView learnersTextView;
    public TextView bioTextView;
    public TextView ratingsTextView;
    public ImageView photoImageView;
    public ImageView disabilityBadge;
    private OnTutorItemClickListener listener;

    public TutorViewHolder(@NonNull View itemView)
    {
        super(itemView);

        nameTextView        = itemView.findViewById(R.id.list_item_tutor_name);
        learnersTextView    = itemView.findViewById(R.id.tutor_list_item_kpi_total_learners);
        bioTextView         = itemView.findViewById(R.id.list_item_tutor_bio);
        ratingsTextView     = itemView.findViewById(R.id.tutor_list_item_kpi_total_ratings);
        photoImageView      = itemView.findViewById(R.id.list_item_tutor_photo);
        disabilityBadge     = itemView.findViewById(R.id.tutor_list_item_kpi_disability);

        itemView.setOnClickListener(v -> {
            if (listener != null)
            {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION)
                    listener.onItemClick(position);
            }
        });
    }

    public void setOnItemClickListener(OnTutorItemClickListener listener) {
        this.listener = listener;
    }
}
