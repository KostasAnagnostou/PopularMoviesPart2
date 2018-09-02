package com.example.android.popularmovies2.retrofit;

import com.example.android.popularmovies2.models.MovieReviewResults;
import com.example.android.popularmovies2.models.MovieVideosResults;
import com.example.android.popularmovies2.models.MoviesResults;
import com.example.android.popularmovies2.utilities.APIUtils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

// The Interface to communicate with Movies Database API
// We are executing a @GET Request
// I used an example for retrofit from here -- > https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792

public interface APIServiceInterface {

    // Get the Movies from the Popular Movies URL
    @GET(APIUtils.POPULAR_MOVIES_URL)
    Call<MoviesResults> getPopularMovies();

    // Get the Movies from the Top Rated Movies URL
    @GET(APIUtils.TOP_RATED_MOVIES_URL)
    Call<MoviesResults> getTopRatedMovies();

    // Get the Videos from the dynamic URL (videosUrl)
    @GET
    Call<MovieVideosResults> getMovieVideos(@Url String videosUrl);

    // Get the Reviews from the dynamic URL (reviewsUrl)
    @GET
    Call<MovieReviewResults> getMovieReviews(@Url String reviewsUrl);
}
