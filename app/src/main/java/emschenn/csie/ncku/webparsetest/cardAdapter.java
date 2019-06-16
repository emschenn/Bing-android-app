package emschenn.csie.ncku.webparsetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class cardAdapter extends RecyclerView.Adapter<cardAdapter.cardViewHolder> {

    private final LayoutInflater mInflater;
    private List<cardData> cards; // Cached copy of words

    cardAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public cardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.card, parent, false);
        return new cardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(cardViewHolder holder, int position) {
        if (cards != null) {
            cardData current = cards.get(position);
            holder.titleView.setText(current.getTitle());
            holder.websiteView.setText(current.getWebsite());
            holder.episodeView.setText(current.getEpisode());
        } else {

        }
    }

    /**
     *     Associate a list of words with this adapter
     */

    void setCards(List<cardData> Cards) {
        cards = Cards;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (cards != null)
            return cards.size();
        else return 0;
    }

    public cardData getCardAtPosition(int position) {
        return cards.get(position);
    }

    class cardViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView websiteView;
        private final TextView episodeView;

        private cardViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            websiteView = itemView.findViewById(R.id.website);
            episodeView = itemView.findViewById(R.id.episode);
        }
    }
}
