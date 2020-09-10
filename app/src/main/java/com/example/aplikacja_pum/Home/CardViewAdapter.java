package com.example.aplikacja_pum.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
//deklaracja
    private ArrayList<CardView> cardViews;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView downloadIMG;
        public TextView title;
        public TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //findbyid
            downloadIMG = (ImageView) itemView.findViewById(R.id.downloadIMG);
            title = (TextView) itemView.findViewById(R.id.image_caption);
            time = (TextView) itemView.findViewById(R.id.image_time_posted);
        }
    }

    public CardViewAdapter(ArrayList<CardView> cardViews, Context context) {
        this.cardViews = cardViews;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView =  cardViews.get(position);

       // UniversalImageLoader.setImage(cardView.getUrl(),holder.downloadIMG,null,"");
        holder.time.setText(cardView.getTime());
        holder.title.setText(cardView.getTitle());
        Log.d("sad",cardView.getUrl());

    }

    @Override
    public int getItemCount() {
        return cardViews.size();
    }


}
