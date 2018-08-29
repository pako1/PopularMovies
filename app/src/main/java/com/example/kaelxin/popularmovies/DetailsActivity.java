package com.example.kaelxin.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.kaelxin.popularmovies.MainActivity.BACKDROP;
import static com.example.kaelxin.popularmovies.MainActivity.DATE;
import static com.example.kaelxin.popularmovies.MainActivity.ID;
import static com.example.kaelxin.popularmovies.MainActivity.LIST;
import static com.example.kaelxin.popularmovies.MainActivity.RATING;
import static com.example.kaelxin.popularmovies.MainActivity.R_LIST;
import static com.example.kaelxin.popularmovies.MainActivity.SYNOPSIS;
import static com.example.kaelxin.popularmovies.MainActivity.THUMBNAIL;
import static com.example.kaelxin.popularmovies.MainActivity.TITLE;

public class DetailsActivity extends AppCompatActivity implements TrailerRecyclerAdapter.ListItemClickListener {

    @BindView(R.id.details_backdrop_iv)
    ImageView backdropImageDetails;
    @BindView(R.id.details_poster_iv)
    ImageView imageDetails;
    @BindView(R.id.title_details_tv)
    TextView titleDetails;
    @BindView(R.id.rating_bv)
    RatingBar averageScoreDetails;
    @BindView(R.id.plot_details_tv)
    TextView plotDetails;
    @BindView(R.id.release_date_details_tv)
    TextView releaseDate;
    @BindView(R.id.recyclervew_review)
    RecyclerView reviewclyer;
    @BindView(R.id.recyclerview_trailers)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    TrailerRecyclerAdapter adapter;
    ArrayList<Trailer> trailers;
    ArrayList<Review> reviewsList;
    ReviewRecyclerAdapter reviewadapter;
    AppDatabase mydb;
    Movie detailedMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_main_activity);

        ButterKnife.bind(this);
        mydb = AppDatabase.getInstance(getApplicationContext());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            detailedMovie = new Movie(extras.getString(ID), extras.getString(TITLE), extras.getString(DATE), extras.getString(RATING),
                    extras.getString(THUMBNAIL), extras.getString(SYNOPSIS), extras.getString(BACKDROP), extras.<Trailer>getParcelableArrayList(LIST), extras.<Review>getParcelableArrayList(R_LIST));
            setTitle(detailedMovie.getTitle());

            Picasso.with(this)
                    .load(detailedMovie.getThumbnail())
                    .placeholder(R.drawable.placeholder)
                    .into(imageDetails);

            Picasso.with(this)
                    .load(detailedMovie.getBackdrop_image())
                    .placeholder(R.drawable.placeholder)
                    .into(backdropImageDetails);

            trailers = detailedMovie.getTrailers();
            reviewsList = detailedMovie.getReviews();
            RecyclerView.LayoutManager firstLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            RecyclerView.LayoutManager secondLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(firstLayout);
            reviewclyer.setLayoutManager(secondLayout);
            adapter = new TrailerRecyclerAdapter(this, trailers, this);
            reviewadapter = new ReviewRecyclerAdapter(this, reviewsList);
            recyclerView.setAdapter(adapter);
            reviewclyer.setAdapter(reviewadapter);
            snapTheList();
            setDataOnViews(detailedMovie);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInDb(detailedMovie)) {
                        mydb.movieDao().deleteMovie(detailedMovie);
                        Toast.makeText(DetailsActivity.this, "movie deleted", Toast.LENGTH_LONG).show();
                    } else {
                        mydb.movieDao().insertMovie(detailedMovie);
                        Toast.makeText(DetailsActivity.this, "movie added", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private boolean checkInDb(Movie detailedMovie) {
        boolean isInDb = false;
        List<Movie> movies = mydb.movieDao().loadAllMovies();
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            if (movie.getId().equals(detailedMovie.getId())) {
                isInDb = true;
            } else {
                isInDb = false;
            }
        }
        return isInDb;
    }


    private void snapTheList() {
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(reviewclyer);
    }

    @Override
    public void onItemclick(int indexItem) {
        Trailer currentTrailer = trailers.get(indexItem);
        String currentYoutubeVideo = currentTrailer.getYoutubeLink();
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
        youtubeIntent.setData(Uri.parse(currentYoutubeVideo));
        String title = getString(R.string.pick);
        Intent chooser = Intent.createChooser(youtubeIntent, title);
        if (youtubeIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    public void setDataOnViews(Movie detailedMovie) {
        averageScoreDetails.setRating(Float.parseFloat(detailedMovie.getVote_average()));
        titleDetails.setText(detailedMovie.getTitle());
        plotDetails.setText(detailedMovie.getPlot_synopsis());
        releaseDate.setText(detailedMovie.getRelease_date());
    }
}

