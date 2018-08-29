package com.example.kaelxin.popularmovies;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieRecyclerAdapter.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener
        , LoaderManager.LoaderCallbacks<List<Movie>> {
    //6 kanoyme implement to interface sthn activity mas gia na mporesoyme na to xrisimopoihsoyme.
    //  kai prepei na ylopoihsoyme tin me8odo tou interface giati akoma den kanei kati apla pernei to index.
    @BindView(R.id.movies_rv)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    public static final String ID = "id";
    public static final String MOVIE = "movie";
    public static final String API_KEY = "api_key";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TITLE = "title";
    public static final String SYNOPSIS = "synopsis";
    public static final String DATE = "date";
    public static final String RATING = "rating";
    public static final String BACKDROP = "backdrop";
    public static final String MY_API_KEY = "cde8c676f288ff7de6f9e1fb957f5511";
    public static final String LIST = "List";
    public static final String R_LIST = "REVIEW_LIST";
    public static final String BASE_URL = "http://api.themoviedb.org/3";

    RecyclerView.LayoutManager layoutManager;
    private MovieRecyclerAdapter adapter;
    private List<Movie> movies = new ArrayList<>();
    private LoaderManager loaderManager;
    private AppDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mydb = AppDatabase.getInstance(getApplicationContext());
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        connectInternet(this);
        adapter = new MovieRecyclerAdapter(this, movies, this);
        //creating a layoutmanager with grid style.
        layoutManager = new GridLayoutManager(this, 2);
        //passing the layoutmanager(gridview) in the recyclerview
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    //7. ylopoioyme ti kanei afou kanoyme to click.
    @Override
    public void onItemClick(int itemIndex) {
        Movie currentMovie = movies.get(itemIndex);
        ArrayList<Trailer> trailer = currentMovie.getTrailers();
        ArrayList<Review> review = currentMovie.getReviews();
        Intent activityIntent = new Intent(this, DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LIST, trailer);
        bundle.putParcelableArrayList(R_LIST, review);
        bundle.putString(ID, currentMovie.getId());
        bundle.putString(BACKDROP, currentMovie.getBackdrop_image());
        bundle.putString(THUMBNAIL, currentMovie.getThumbnail());
        bundle.putString(TITLE, currentMovie.getTitle());
        bundle.putString(SYNOPSIS, currentMovie.getPlot_synopsis());
        bundle.putString(DATE, currentMovie.getRelease_date());
        bundle.putString(RATING, currentMovie.getVote_average());
        activityIntent.putExtras(bundle);
        startActivity(activityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sorting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_movie_key))) {
            String movieType = sharedPreferences.getString(key, getString(R.string.movies_default));
            if (movieType.equals(getString(R.string.pref_movie_favorite_movies_value))) {
                movies = mydb.movieDao().loadAllMovies();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addList(movies);
                    }
                });
            } else {
                loaderManager.restartLoader(0, null, this);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }


    private void connectInternet(Context context) {
        ConnectivityManager connection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String movieType = preferences.getString(getString(R.string.pref_movie_key), getString(R.string.movies_default));
        NetworkInfo wifi;
        NetworkInfo mobile;
        if (connection != null) {
            wifi = connection.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobile = connection.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            boolean isConnected = (wifi != null && wifi.isConnected()) ||
                    (mobile != null && mobile.isConnected() && mobile.isConnectedOrConnecting());
            if (isConnected) {
                if (movieType.equals(getString(R.string.pref_movie_favorite_movies_value))) {
                    movies =mydb.movieDao().loadAllMovies();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addList(movies);
                        }
                    });

                } else {
                    loaderManager = getLoaderManager();
                    loaderManager.restartLoader(0, null, this);
                }
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String movieType = preferences.getString(getString(R.string.pref_movie_key), getString(R.string.movies_default));
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder movieQuery = baseUri.buildUpon();
        movieQuery.appendPath(MOVIE)
                .appendPath(movieType)
                .appendQueryParameter(API_KEY, MY_API_KEY);
        return new MovieLoader(this, movieQuery.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movieList) {
        progressBar.setVisibility(View.GONE);
        movies = movieList;
        if (movieList != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
            adapter.addList(movieList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        adapter.clear();
    }
}
