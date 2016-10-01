package rocks.ecox.popularmovies;

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
import java.util.Arrays;
import java.util.List;

import static rocks.ecox.popularmovies.BuildConfig.TMDB_API_KEY;

public class FetchMovieTask extends AsyncTask<String, Void, String[]> {
    private final String TAG = "API";

    private String[] getMovieDataFromJson(String movieJsonStr)
        throws JSONException{
        // Is there a way to request the number of movies by passing in int numMovies?
        final String TMDB_LIST = "results";
        final String TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String DURATION = ""; // Haven't found this in JSON yet
        final String sizeList[] = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};
        final String POSTER_SIZE = sizeList[2];
        final String THUMBNAIL_SIZE = sizeList[0];
        final String POSTER_URL = "http://image.tmdb.org/t/p/" + POSTER_SIZE;
        final String THUMBNAIL_URL = "http://image.tmdb.org/t/p/" + THUMBNAIL_SIZE;
        // TODO: Get duration and trailers. Do I need the movie id?

        JSONObject movieJSON = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJSON.getJSONArray(TMDB_LIST);

        String[] resultStrs = new String[movieArray.length()];
        for(int i = 0; i < movieArray.length(); i++) {
            String title;

            JSONObject movieJson = movieArray.getJSONObject(i);

            Movie movie = new Movie();
            movie.setTitle(movieJson.getString(TITLE));
            movie.setPoster(POSTER_URL + movieJson.getString(POSTER_PATH));
            movie.setThumbnail(THUMBNAIL_URL + movieJson.getString(POSTER_PATH));
            movie.setReleaseDate(movieJson.getString(POSTER_PATH));
            movie.setUserRating(movieJson.getString(RELEASE_DATE));
            movie.setOverview(movieJson.getString(OVERVIEW));
            movie.save();

//            Log.d("DB", "" + movie.getTitle() + " " + movie.getUserRating()); // This line breaks it
        }
        return resultStrs; // TODO: remove this placeholder, it does nothing.
    }

    @Override
    protected String[] doInBackground(String... params){
        final List<String> queryList = Arrays.asList("popular", "top_rated");

        if(!queryList.contains(params[0]) ) {
            Log.d(TAG, "doInBackground sortOrder parameter incorrect. " + params[0]);
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/";
            final String QUERY_PARAM = "movie/" + queryList.get(0); // TODO: pass this in via params
            final String API_KEY = "?api_key=" + TMDB_API_KEY;
            URL url = new URL(BASE_URL.concat(QUERY_PARAM).concat(API_KEY));

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                Log.e(TAG, "Input Stream is empty");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                Log.e(TAG, "Buffer length was zero");
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
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
            return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        Log.d(TAG, "JSON: " + movieJsonStr);
        return null;
    }
}
