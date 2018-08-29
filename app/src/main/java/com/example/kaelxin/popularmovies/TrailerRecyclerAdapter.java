package com.example.kaelxin.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.TrailerViewHolder> {

    public static final String YOUTUBE_BASE_PATH = "https://img.youtube.com/vi/";
    public static final String YOUTUBE_IMAGE_DIR = "/hqdefault.jpg";
    private Context context;
    ArrayList<Trailer> trailers;

    private final ListItemClickListener itemClickListener;

    public interface ListItemClickListener {
        void onItemclick(int indexItem);
    }


    TrailerRecyclerAdapter(Context context, ArrayList<Trailer> trailers, ListItemClickListener listItemClickListener) {
        this.context = context;
        this.trailers = trailers;
        this.itemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_cardview, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

        Trailer currentTrailer = trailers.get(position);
        String youtube_image = YOUTUBE_BASE_PATH + currentTrailer.getKey() + YOUTUBE_IMAGE_DIR;
        Picasso.with(context).load(youtube_image).placeholder(R.drawable.placeholder).into(holder.movieTrailerIv);
        holder.trailer_name_tv.setText(currentTrailer.getName());

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @BindView(R.id.trailer_iv)
        ImageView movieTrailerIv;

        @BindView(R.id.trailer_name_tv)
        TextView trailer_name_tv;

        @Override
        public void onClick(View v) {
            itemClickListener.onItemclick(getAdapterPosition());
        }
    }
}
