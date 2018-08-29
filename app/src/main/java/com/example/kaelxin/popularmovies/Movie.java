package com.example.kaelxin.popularmovies;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
@Entity(tableName = "movies")
public class Movie {
    @NonNull
    @PrimaryKey
    private String id;
    private String title;
    private String release_date;
    private String vote_average;
    private String thumbnail;
    private String plot_synopsis;
    private String backdrop_image;
    @Ignore
    private ArrayList<Trailer> trailers;
    @Ignore
    private ArrayList<Review> reviews;

    @Ignore
    public Movie(String id, String title, String release_date, String vote_average, String thumbnail, String plot_synopsis, String backdrop_image, ArrayList<Trailer> trailers, ArrayList<Review> reviews) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.thumbnail = thumbnail;
        this.plot_synopsis = plot_synopsis;
        this.backdrop_image = backdrop_image;
        this.trailers = trailers;
        this.reviews = reviews;
    }

    public Movie(String id, String title, String release_date, String vote_average, String thumbnail, String plot_synopsis, String backdrop_image) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.thumbnail = thumbnail;
        this.plot_synopsis = plot_synopsis;
        this.backdrop_image = backdrop_image;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

    public void setBackdrop_image(String backdrop_image) {
        this.backdrop_image = backdrop_image;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public String getBackdrop_image() {
        return backdrop_image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }
}
