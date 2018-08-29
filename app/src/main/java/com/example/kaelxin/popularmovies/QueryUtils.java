package com.example.kaelxin.popularmovies;


import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.kaelxin.popularmovies.MainActivity.API_KEY;
import static com.example.kaelxin.popularmovies.MainActivity.MOVIE;
import static com.example.kaelxin.popularmovies.MainActivity.MY_API_KEY;

public final class QueryUtils {

    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w500";
    private static final String IMAGE_SIZE_ORIGINAL = "original";
    private static final String VIDEOS = "videos";
    private static final String BASE_VIDEO_URL = "https://api.themoviedb.org/3";
    private static final String YOUTUBE_PATH = "https://www.youtube.com/watch?v=";
    private static final String REVIEWS = "reviews";

    private QueryUtils() {
    }

    public static List<Movie> fetchData(String requestURL) {

        URL url = createURL(requestURL);
        String JsonResponse = makeHttpConnection(url);

        return fetchJson(JsonResponse);
    }

    private static URL createURL(String requestURL) {
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpConnection(URL url) {

        String JsonResponse = "";
        HttpURLConnection httpURLConnection;
        InputStream inputStream;

        if (url == null) {
            return JsonResponse;
        }

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(1000);
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                JsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return JsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return builder.toString();
    }

    private static List<Movie> fetchJson(String requestURL) {
        List<Movie> movieList = new ArrayList<>();
        String vote_average;
        String title;
        String release_date;
        String plot;
        String poster_path;
        String backdrop_image;
        String id;
        ArrayList<Review> reviews;
        ArrayList<Trailer> trailers;
        try {
            JSONObject rootObject = new JSONObject(requestURL);
            JSONArray results = rootObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonObject = results.getJSONObject(i);
                title = jsonObject.getString("title");
                release_date = jsonObject.getString("release_date");
                vote_average = jsonObject.getString("vote_average");
                plot = jsonObject.getString("overview");
                id = jsonObject.getString("id");
                trailers = getTrailer(id);
                reviews = getReviews(id);
                poster_path = BASE_URL + IMAGE_SIZE + jsonObject.getString("poster_path");
                backdrop_image = BASE_URL + IMAGE_SIZE_ORIGINAL + jsonObject.getString("backdrop_path");
                movieList.add(new Movie(id, title, release_date, vote_average, poster_path, plot, backdrop_image, trailers, reviews));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }

    private static ArrayList<Review> getReviews(String id) {
        Uri reviewURL = Uri.parse(BASE_VIDEO_URL);
        Uri.Builder builder = reviewURL.buildUpon();
        builder.appendPath(MOVIE)
                .appendPath(id)
                .appendPath(REVIEWS)
                .appendQueryParameter(API_KEY, MY_API_KEY);
        URL reviewsURL = createURL(builder.toString());
        return fetchReviews(makeHttpConnection(reviewsURL));
    }

    private static ArrayList<Review> fetchReviews(String jsonResponse) {
        ArrayList<Review> reviewList = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(jsonResponse);
            JSONArray results = rootObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject reviewObject = results.getJSONObject(i);
                reviewList.add(new Review(reviewObject.getString("author"), reviewObject.getString("content")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }

    private static ArrayList<Trailer> getTrailer(String id) {
        Uri trailerURL = Uri.parse(BASE_VIDEO_URL);
        Uri.Builder builder = trailerURL.buildUpon();
        builder.appendPath(MOVIE)
                .appendPath(id)
                .appendPath(VIDEOS)
                .appendQueryParameter(API_KEY, MY_API_KEY);
        URL trailerUrl = createURL(builder.toString());
        String JsonResponse = makeHttpConnection(trailerUrl);
        return fetchTrailers(JsonResponse, id);
    }

    private static ArrayList<Trailer> fetchTrailers(String jsonResponse, String id) {
        ArrayList<Trailer> trailerList = new ArrayList<>();
        String key;
        String name;
        String youtubeLInk;
        try {
            JSONObject rootObject = new JSONObject(jsonResponse);
            JSONArray results = rootObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject trailerJsonObject = results.getJSONObject(i);
                key = trailerJsonObject.getString("key");
                name = trailerJsonObject.getString("name");
                youtubeLInk = YOUTUBE_PATH + key;
                trailerList.add(new Trailer(id, key, name, youtubeLInk));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerList;
    }


}
