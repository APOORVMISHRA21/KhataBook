package Adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.khatabook.R;
import com.opensooq.pluto.base.PlutoAdapter;
import com.opensooq.pluto.base.PlutoViewHolder;
import com.opensooq.pluto.listeners.OnItemClickListener;

import java.util.List;

import Models.SampleCaraouselCard;

public class CaraouselAdapter extends PlutoAdapter<SampleCaraouselCard, CaraouselAdapter.CaraouselViewHolder> {

    Context context;

    public CaraouselAdapter(List<SampleCaraouselCard> items, Context context) {
        super(items);
        this.context = context;
    }


    @Override
    public CaraouselViewHolder getViewHolder(ViewGroup viewGroup, int i) {
        return new CaraouselViewHolder(viewGroup, R.layout.sample_caraousel_card_onboarding);
    }

    public static class CaraouselViewHolder extends PlutoViewHolder<SampleCaraouselCard>{

        ImageView cardPoster;
        TextView cardTitle, cardSubTitle;

        public CaraouselViewHolder(ViewGroup parent, int itemLayoutId){
            super(parent, itemLayoutId);
            cardPoster = getView(R.id.card_poster);
            cardTitle = getView(R.id.card_title);
            cardSubTitle = getView(R.id.card_subtitle);
        }

        @Override
        public void set(SampleCaraouselCard sampleCaraouselCard, int i) {

            Glide.with(getContext())
                    .load(sampleCaraouselCard.getResourceId())
                    .into(cardPoster);

            cardTitle.setText(sampleCaraouselCard.getTitleText());

            cardSubTitle.setText(sampleCaraouselCard.getSubTitleText());
        }
    }
}
