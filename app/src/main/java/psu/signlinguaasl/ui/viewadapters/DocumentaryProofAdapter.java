package psu.signlinguaasl.ui.viewadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.models.DocumentaryProof;
import psu.signlinguaasl.ui.viewholders.DocumentaryProofViewHolder;

public class DocumentaryProofAdapter extends RecyclerView.Adapter<DocumentaryProofViewHolder>
{
    private final List<DocumentaryProof> educationList;

    public DocumentaryProofAdapter(List<DocumentaryProof> educationList)
    {
        this.educationList = educationList;
    }

    @NonNull
    @Override
    public DocumentaryProofViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_doc_proof, parent, false);
        return new DocumentaryProofViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentaryProofViewHolder holder, int position)
    {
        DocumentaryProof doc = educationList.get(position);

        holder.txv_docproofYear.setText(doc.getDocproofYear());
        holder.txv_docproofTitle.setText(doc.getDocproofTitle());
        holder.txv_docproofCaption.setText(doc.getDocproofCaption());
    }

    @Override
    public int getItemCount()
    {
        return educationList.size();
    }
}

