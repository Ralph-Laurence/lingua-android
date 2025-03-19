package psu.signlinguaasl.ui.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import psu.signlinguaasl.R;

public class DocumentaryProofViewHolder extends RecyclerView.ViewHolder
{
    public TextView txv_docproofYear, txv_docproofTitle, txv_docproofCaption;

    public DocumentaryProofViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txv_docproofYear    = itemView.findViewById(R.id.txv_docproof_year);
        txv_docproofTitle   = itemView.findViewById(R.id.txv_docproof_title);
        txv_docproofCaption = itemView.findViewById(R.id.txv_docproof_caption);
    }
}