package com.example.kaelxin.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    //2. arxikopoiw mia matabliti toy tupou listener.
    private final ListItemClickListener mListener;

    //1. ftiaxnw ena interface me mia methodo pou 8a pairnei tin 8esi tou sigkekrimenou antikeimenou poy 8a ginetai klik.
    public interface ListItemClickListener {
        void onItemClick(int itemIndex);
    }

    //3. tin pernaw ston constructor gia na sindeetai o adaptoras me to activity.
    MovieRecyclerAdapter(Context context, List<Movie> movieList, ListItemClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cardview, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie currentMovie = movieList.get(position);

        Picasso.with(context)
                .load(currentMovie.getThumbnail())
                .placeholder(R.drawable.placeholder)
                .into(holder.moviePosterIv);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // 4. viewholder exei onclicklistener kai krataei ta views poy blepoyme stin o8oni mas.
    //    kanoyme implement to onclicklistener kai prepei na ylopoihsoyme thn me8odo ayth.
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @BindView(R.id.movie_iv)
        ImageView moviePosterIv;
        //5. otan 8a kanoyme click sto antikeimeno tote 8a kaloyme tin me8odo tou interface
        //   kai 8a tou bazoyme thn 8esi apo thn lista (index tou antikeimenou)

        @Override
        public void onClick(View view) {
            mListener.onItemClick(getAdapterPosition());

        }
    }

    public void addList(List<Movie> movies) {
        movieList = movies;
        this.notifyDataSetChanged();
    }

    public void clear() {
        final int size = movieList.size();
        movieList.clear();
        notifyItemRangeRemoved(0, size);
    }

}
