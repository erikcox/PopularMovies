/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static rocks.ecox.popularmovies.BuildConfig.TMDB_API_KEY;

/**
 * Creates an AsyncTask to grab movie data and writes it to a movie object.
 */

public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private final String TAG = FetchMovieTask.class.getSimpleName();
    private int pageNum = 1;

    /** Turns Movie DB's JSON string into Movie objects and populates SQLite DB */
    private static void getMovieDataFromJson(String movieJsonStr)
            throws JSONException, ParseException {
        final String MOVIE_LIST = "results";
        final String MOVIE_ID = "id";
        // Spec says to use 'original_title'. Note that there is also a 'title' field.
        final String TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String RATING = "vote_average";

        final String sizeList[] = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};
        final String POSTER_SIZE = sizeList[2];
        final String THUMBNAIL_SIZE = sizeList[0];

        final String POSTER_URL = "http://image.tmdb.org/t/p/" + POSTER_SIZE;
        final String THUMBNAIL_URL = "http://image.tmdb.org/t/p/" + THUMBNAIL_SIZE;
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        JSONObject movieJSON = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJSON.getJSONArray(MOVIE_LIST);

        ArrayList<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movieJson = movieArray.getJSONObject(i);
            Movie movie = new Movie(Long.parseLong(movieJson.getString(MOVIE_ID)), movieJson.getString(TITLE),
                    movieJson.getString(POSTER_PATH), (POSTER_URL + movieJson.getString(POSTER_PATH)),
                    (THUMBNAIL_URL + movieJson.getString(POSTER_PATH)),
                    dFormat.parse(movieJson.getString(RELEASE_DATE)), movieJson.getString(RATING),
                    movieJson.getString(OVERVIEW));

            movie.save();
        }
    }

    /** Returns the JSON string of movie data. */
    @Override
    protected ArrayList<Movie> doInBackground(String... params){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;
        String api_key = TMDB_API_KEY;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String LANGUAGE = "language";
            final String PAGE = "page";
            final String SORT_PARAM = "sort_by";
            final String VOTE_COUNT_THRESHOLD = "vote_count.gte";
            final String API_KEY_PARAM = "api_key";

            Uri.Builder uriBuilder = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(LANGUAGE, "en")
                    .appendQueryParameter(PAGE, params[1])
                    .appendQueryParameter(SORT_PARAM, params[0]+".desc")
                    .appendQueryParameter(API_KEY_PARAM, api_key);

            // If sorted by rating return 500
            if (params[0].equals("vote_average")) {
                uriBuilder.appendQueryParameter(VOTE_COUNT_THRESHOLD, "500");
            }

            Uri completeUri = uriBuilder.build();
            URL url = new URL(completeUri.toString());
            Log.d(TAG, "Getting url: " + completeUri.toString());

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Log.e(TAG, "Input Stream is empty");
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                Log.e(TAG, "Buffer length was zero");
                return null;
            }

            movieJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getMovieDataFromJson(movieJsonStr);
        } catch (JSONException | ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }
}
