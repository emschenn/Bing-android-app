package emschenn.csie.ncku.webparsetest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SONU on 25/09/15.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
    // View holder for gridview recycler view as we used in listview
    public TextView title;
    public TextView episode;
    public TextView website;

    public RecyclerViewHolder(View view) {
        super(view);
        this.title = view.findViewById(R.id.title);
        this.episode = view.findViewById(R.id.episode);
        this.website = view.findViewById(R.id.website);
    }
}