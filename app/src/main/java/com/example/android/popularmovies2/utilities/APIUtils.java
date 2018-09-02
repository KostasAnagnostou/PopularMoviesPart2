package com.example.android.popularmovies2.utilities;

// I used Retrofit Example from here ---> https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792

import com.example.android.popularmovies2.retrofit.APIServiceInterface;
import com.example.android.popularmovies2.retrofit.RetrofitClient;

public class APIUtils {

    // The Base Url (from themoviedb.org API) for the Movies
    public static final String MOVIES_BASE_API_URL = "https://api.themoviedb.org/3/movie/";

    // The API_KEY Param and the API_KEY Value
    public static final String API_KEY_PARAM = "?api_key=";
    public static final String API_KEY = "";// you should aquire an api?key from TheMovieDb.org API

    // The POPULAR MOVIES URL Path
    public static final String POPULAR_MOVIES_URL = MOVIES_BASE_API_URL + "popular" + API_KEY_PARAM + API_KEY;

    // The TOP RATED MOVIES URL Path
    public static final String TOP_RATED_MOVIES_URL = MOVIES_BASE_API_URL + "top_rated" + API_KEY_PARAM + API_KEY;

    // Base Image Path and Image Size
    public static final String BASE_IMAGE_PATH = "http://image.tmdb.org/t/p/w342/";

    // The videos segment for the url
    public static final String MOVIE_VIDEOS_PATH = "/videos";

    // The reviews segment for the url
    public static final String MOVIE_REVIEWS_PATH = "/reviews";

    // The URL's/paths for the YOUTUBE Videos and Images
    public static final String YOUTUBE_BASE_VIDEO_URL = "https://www.youtube.com/watch?v=";
    public static final String YOUTUBE_BASE_IMAGE_URL = "https://img.youtube.com/vi/";
    public static final String YOUTUBE_JPG_ENDING_URL = "/1.jpg";
    public static final String YOUTUBE_BASE_APP_URL = "vnd.youtube:";

    public static APIServiceInterface getAPIInterface() {
        return RetrofitClient.getClient(MOVIES_BASE_API_URL).create(APIServiceInterface.class);
    }
}
