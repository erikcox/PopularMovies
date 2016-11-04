/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies.utilities;

import static rocks.ecox.popularmovies.BuildConfig.TMDB_API_KEY;

/**
 * Constant values utility class
 */

public class Constants {

    public static final String DISCOVER_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/%s";
    public static final String API_KEY = TMDB_API_KEY;
    public static final String APPEND_API_KEY = "?api_key=" + TMDB_API_KEY;

    public static final String REVIEW_BASE_URL = "http://api.themoviedb.org/3/movie/%s/reviews";
    public static final String TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie/%s/videos";
    public static final String MEDIUM_IMAGE_URL = "https://image.tmdb.org/t/p/w342/%s";
    public static final String THUMBNAIL_URL = "https://image.tmdb.org/t/p/w92/%s";
    public static final String YOUTUBE_URL_BASE = "https://www.youtube.com/watch?v=";

    public static final String RESPONSE_RESULTS = "results";
    public static final String RESPONSE_ID = "id";
    public static final String TOTAL_PAGES = "total_pages";
    public static final String CURRENT_PAGE = "page";
    public static final String TITLE = "title";
    public static final String OVERVIEW = "overview";
    public static final String POPULARITY = "popularity";
    public static final String GENRE = "genres";
    public static final String RELEASE_DATE = "release_date";
    public static final String POSTER_PATH = "poster_path";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String USER_RATING = "vote_average";
    public static final String RUNTIME = "runtime";
    public static final String REVIEWS_RESPENSE_SET = "reviews";
    public static final String REVIEW_AUTHOR = "author";
    public static final String REVIEW_CONTENT = "content";
    public static final String VIDEOS_RESPONSE_SET = "videos";
    public static final String TRAILER_NAME = "name";
    public static final String TRAILER_YOUTUBE = "key";
    public static final String MOVIE_ID = "movieId";
    public static final String NETWORK_FAIL = "Network connection unavailable.";
}
