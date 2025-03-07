package psu.signlinguaasl.ui.viewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

import psu.signlinguaasl.R;

public class EntryActivityViewPagerAdapter
       extends RecyclerView.Adapter<EntryActivityViewPagerAdapter.ViewHolder>
{
    private final LayoutInflater mInflater;
    private final int m_itemsCount;

    // public Consumer<ViewHolder> OnViewHolderInflated; // SImilar to C# Action<Obj>
    public ViewHolder inflatedView;

    public EntryActivityViewPagerAdapter(Context context, int itemsCount)
    {
        this.mInflater = LayoutInflater.from(context);
        m_itemsCount   = itemsCount;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        int layout;

        switch (viewType)
        {
            case 1:
                layout = R.layout.activity_entry_page1;
                break;

            case 2:
                layout = R.layout.activity_entry_page2;
                break;

            case 3:
                layout = R.layout.activity_entry_page3;
                break;

            case 4:
                layout = R.layout.activity_entry_page4;
                break;

            case 0:
            default:
                layout = R.layout.activity_entry_page0;
                break;
        }

        view = mInflater.inflate(layout, parent, false);
        inflatedView = new ViewHolder(view);

//        if (OnViewHolderInflated != null)
//            OnViewHolderInflated.accept(inflatedView);

        return  inflatedView;
    }

    @Override
    public int getItemViewType(int position) {
        // Return the view type based on the position
        return position % m_itemsCount; // Assuming you have 5 different layouts
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind data to the UI elements here
    }

    @Override
    public int getItemCount() {
        return m_itemsCount;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // UI elements for item view

        private final View m_itemView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            // Initialize UI elements here

            m_itemView = itemView;
        }

        public View FindView(int viewId)
        {
            if (m_itemView == null)
                return null;

            return m_itemView.findViewById(viewId);
        }
    }
}
