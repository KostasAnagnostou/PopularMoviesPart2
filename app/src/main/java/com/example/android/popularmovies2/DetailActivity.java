package com.example.android.popularmovies2;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies2.adapters.ReviewAdapter;
import com.example.android.popularmovies2.adapters.VideoAdapter;
import com.example.android.popularmovies2.data.MovieContract;
import com.example.android.popularmovies2.models.Movie;
import com.example.android.popularmovies2.models.MovieReview;
import com.example.android.popularmovies2.models.MovieReviewResults;
import com.example.android.popularmovies2.models.MovieVideo;
import com.example.android.popularmovies2.models.MovieVideosResults;
import com.example.android.popularmovies2.retrofit.APIServiceInterface;
import com.example.android.popularmovies2.utilities.APIUtils;
import com.example.android.popularmovies2.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements
        VideoAdapter.VideoAdapterOnClickHandler,
        ReviewAdapter.ReviewAdapterOnClickHandler {

    private APIServiceInterface mAPIService;

    // Create and bind the views with ButterKnife Library
    @BindView(R.id.movie_synopsis_detail_tv)
    TextView mSynopsisTextView;

    @BindView(R.id.movie_poster_image_detail_iv)
    ImageView mPosterImageView;

    @BindView(R.id.movie_user_rating_tv)
    TextView mUserRating;

    @BindView(R.id.detail_rating_bar)
    RatingBar mRatingBar;

    @BindView(R.id.movie_release_date_tv)
    TextView mReleaseDate;

    @BindView(R.id.video_recycler_view)
    RecyclerView mVideoRecyclerView;

    @BindView(R.id.review_recycler_view)
    RecyclerView mReviewRecyclerView;

    @BindView(R.id.fab_favorite)
    FloatingActionButton fab;

    private String movieApiId;
    private List<MovieVideo> mVideos;
    private List<MovieReview> mReviews;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;
    private Movie mMovie;
    private boolean isFavoriteMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Bundle dataMovie = getIntent().getExtras();
        mMovie = dataMovie.getParcelable("movie");
        movieApiId = mMovie.getApiId();

        // Initialize an instance of the APIServiceInterface
        mAPIService = APIUtils.getAPIInterface();
        mVideoAdapter = new VideoAdapter(this, new ArrayList<MovieVideo>(0), this);
        mReviewAdapter = new ReviewAdapter(this, new ArrayList<MovieReview>(0), this);

        populateUI();
        fetchMovieVideos();
        fetchMovieReviews();
        // Query the Database Movies where movie_id = movieApiID
        new queryAsyncTask().execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavoriteMovie) {
                    showInsertConfirmationDialog();
                    setFabIcon();
                } else {
                    showDeleteConfirmationDialog();
                    setFabIcon();
                }
            }
        });
    }

    private void populateUI() {
        // Get the Movie Title
        String title = mMovie.getTitle();
        // Set the Movie Title as Application Title
        setTitle(title);

        // Get and set the Plot Synopsis
        String synopsis = mMovie.getSynopsis();
        mSynopsisTextView.setText(synopsis);

        // Get and set the Movie Poster Image
        String imageThumbnail = mMovie.getThumbnail();
        String imageThumbnailPath = APIUtils.BASE_IMAGE_PATH + imageThumbnail;
        Picasso.with(this)
                .load(imageThumbnailPath)
                .into(mPosterImageView);

        // Set the Content Description for the Movie Poster Image
        String imageContentDescription = title + getString(R.string.poster_image_cd);
        mPosterImageView.setContentDescription(imageContentDescription);

        // Get the User Rating
        String userRating = mMovie.getUserRating();
        // Set the TextView for User Rating adding the "/10"
        String userTextRating = userRating + getString(R.string.rating_ten);
        mUserRating.setText(userTextRating);

        /* Convert the userRating String to float
           and set the Rating Bar */
        float ratingBar = MovieUtils.movieRating(userRating);
        mRatingBar.setRating(ratingBar);

        // Get and set the ReleaseDate
        String releaseDate = mMovie.getReleaseDate();
        mReleaseDate.setText(releaseDate);

        setFabIcon();

        LinearLayoutManager layoutManagerVideo = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);

        mVideoRecyclerView.setLayoutManager(layoutManagerVideo);
        mReviewRecyclerView.setLayoutManager(layoutManagerReview);

        mVideoRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setHasFixedSize(true);

        // Set the adapters on each RecyclerView, so the lists can be populated in the UI
        mVideoRecyclerView.setAdapter(mVideoAdapter);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
    }

    // This Method fetches and displays the Movie Videos - (Async Call with Retrofit2)
    private void fetchMovieVideos() {
        // Construct the Url(as String) for  the videos
        String videosUrl = APIUtils.MOVIES_BASE_API_URL + movieApiId +
                APIUtils.MOVIE_VIDEOS_PATH + APIUtils.API_KEY_PARAM + APIUtils.API_KEY;

        mAPIService.getMovieVideos(videosUrl).enqueue(new Callback<MovieVideosResults>() {
            @Override
            public void onResponse(Call<MovieVideosResults> call, Response<MovieVideosResults> response) {

                if (response.isSuccessful()) {
                    // Load the Popular Movies
                    mVideos = response.body().getResults();
                    mVideoAdapter.updateVideos(mVideos);
                    Log.d("MainActivity", "Videos loaded from API");
                } else {
                    int statusCode = response.code();
                    Log.d("MainActivity", "Status Code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<MovieVideosResults> call, Throwable t) {
                Log.d("MainActivity", "error loading Videos from API");
            }
        });
    }

    /* When a video is clicked, this method opens the Youtube Video
     * in the Youtube app or in a web browser
     */
    @Override
    public void onClick(int clickedVideoIndex) {

        MovieVideo video = mVideos.get(clickedVideoIndex);
        String youtubeKey = video.getKey();

        Intent appYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse(APIUtils.YOUTUBE_BASE_APP_URL + youtubeKey));
        Intent webYoutube = new Intent(Intent.ACTION_VIEW,
                Uri.parse(APIUtils.YOUTUBE_BASE_VIDEO_URL + youtubeKey));
        try {
            startActivity(appYoutube);
        } catch (ActivityNotFoundException ex) {
            startActivity(webYoutube);
        }
    }

    // This Method fetches and displays the Movie Reviews - (Async Call with Retrofit2)
    private void fetchMovieReviews() {
        // Construct the Url(as String) for the Reviews
        String reviewsUrl = APIUtils.MOVIES_BASE_API_URL + movieApiId +
                APIUtils.MOVIE_REVIEWS_PATH + APIUtils.API_KEY_PARAM + APIUtils.API_KEY;

        mAPIService.getMovieReviews(reviewsUrl).enqueue(new Callback<MovieReviewResults>() {
            @Override
            public void onResponse(Call<MovieReviewResults> call, Response<MovieReviewResults> response) {

                if (response.isSuccessful()) {
                    // Load the Reviews
                    mReviews = response.body().getResults();
                    mReviewAdapter.loadReviews(mReviews);
                    Log.d("MainActivity", "Reviews loaded from API");
                } else {
                    int statusCode = response.code();
                    Log.d("MainActivity", "Status Code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<MovieReviewResults> call, Throwable t) {
                Log.d("MainActivity", "error loading Reviews from API");
            }
        });
    }

    /* In this Method we can do something
     * when a review is clicked
     */
    @Override
    public void onClickReview(int clickedReviewIndex) {
    }

    /* Query the Database Movies where movie API ID = movieApiID
     * to see if this movie is inside the Database
     * if it is then is a Favorite Movie */
    private class queryAsyncTask extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {

            // The Content URI for the Movies Table
            Uri uri = MovieContract.MovieEntry.CONTENT_URI;

            String selection = MovieContract.MovieEntry.COLUMN_MOVIE_API_ID + "=?";
            String[] selectionArgs = new String[]{movieApiId};

            return getContentResolver().query(uri,
                    null,
                    selection,
                    selectionArgs,
                    null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if (cursor.getCount() > 0) {
                isFavoriteMovie = true;
                setFabIcon();
            } else {
                isFavoriteMovie = false;
                setFabIcon();
            }
        }
    }

    /*  This method creates a dialog asking the user to confirm
      before adding a movie in Favorite List  */
    private void showInsertConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        String insertDialogMsg = String.format(getResources().getString(R.string.insert_dialog_message), mMovie.getTitle());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(insertDialogMsg);
        builder.setPositiveButton(R.string.insert, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Add" button, so insert movie to favorite list
                insertMovieToDB();
                isFavoriteMovie = true;
                setFabIcon();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*  This method creates a dialog asking the user to confirm
      before deleting a movie from Favorite List  */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        String deleteDialogMsg = String.format(getResources().getString(R.string.delete_dialog_message), mMovie.getTitle());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(deleteDialogMsg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the products.
                deleteMovieFromDB();
                isFavoriteMovie = false;
                setFabIcon();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Method to insert a movie in the database
    private void insertMovieToDB() {

        String title = mMovie.getTitle();
        String synopsis = mMovie.getSynopsis();
        String movieImagePoster = mMovie.getThumbnail();
        String ratingText = mMovie.getUserRating();
        String releaseDate = mMovie.getReleaseDate();
        String movieApiId = mMovie.getApiId();

        // Insert new movie data
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, synopsis);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, ratingText);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, movieImagePoster);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_API_ID, movieApiId);

        // Insert the content values via a ContentResolver
        Uri newUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        // Show a toast message depending on whether or not the delete was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "The Insertion Failed",
                    Toast.LENGTH_SHORT).show();
        } else { // The Movie inserted  to the database
            String favorite_message = String.format(getResources().getString(R.string.favorite_movie_message), title);
            Toast.makeText(DetailActivity.this, favorite_message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Perform the deletion of the product in the database.
     */
    private void deleteMovieFromDB() {

        Uri uri = MovieContract.BASE_CONTENT_URI.buildUpon()
                .appendPath(MovieContract.PATH_MOVIES)
                .appendPath(movieApiId)
                .build();

        // Delete a single row of data using a ContentResolver
        int rowsDeleted = getContentResolver().delete(uri, null, null);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, "The Deletion Failed",
                    Toast.LENGTH_SHORT).show();
        } else { //The Movie deleted from the database
            String delete_movie_message = String.format(getResources().getString(R.string.delete_favorite_movie_message), mMovie.getTitle());
            Toast.makeText(DetailActivity.this, delete_movie_message, Toast.LENGTH_SHORT).show();
        }

        // This Method finish() and restarts the activity
        finish();
    }

    private void setFabIcon() {
        if (!isFavoriteMovie) {
            fab.setImageResource(R.drawable.ic_no_favorite_fab_24px);
        } else {
            fab.setImageResource(R.drawable.ic_favorite_fab_icon);
        }
    }

    // This method provides similar behavior like the back button phone
    // when the UP button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }
}