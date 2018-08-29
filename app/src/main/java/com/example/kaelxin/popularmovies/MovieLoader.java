package com.example.kaelxin.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private String stringQuery;
    private List<Movie> movieList;

    public MovieLoader(Context context, String stringQuery) {
        super(context);
        this.stringQuery = stringQuery;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (movieList != null) {
            deliverResult(movieList);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> movies = null;
        if (stringQuery != null) {
            movies = QueryUtils.fetchData(stringQuery);
        }
        return movies;
    }

    @Override
    public void deliverResult(List<Movie> data) {
        movieList = data;
        super.deliverResult(data);
    }
}
