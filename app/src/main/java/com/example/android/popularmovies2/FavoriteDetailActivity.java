package com.example.android.popularmovies2;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies2.data.MovieContract;
import com.example.android.popularmovies2.utilities.APIUtils;
import com.example.android.popularmovies2.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // The columns to return for each row on Movies Table
    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_API_ID
    };
    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query.
     */
    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_SYNOPSIS = 2;
    public static final int INDEX_MOVIE_RATING = 3;
    public static final int INDEX_MOVIE_IMAGE = 4;
    public static final int INDEX_MOVIE_RELEASE_DATE = 5;
    public static final int INDEX_MOVIE_API_ID = 6;
    private static final String LOG_TAG = FavoriteDetailActivity.class.getName();
    /*
     * This ID will be used to identify the Loader responsible for loading the movie details
     * for a particular Movie
     * */
    private static final int ID_DETAIL_LOADER = 353;
    @BindView(R.id.favorite_poster_image_detail)
    ImageView mPosterImage;
    @BindView(R.id.fab_favorite_movie)
    FloatingActionButton fab;
    @BindView(R.id.favorite_detail_rating_bar)
    RatingBar mRatingBar;
    @BindView(R.id.favorite_user_rating_tv)
    TextView mUserRating;
    @BindView(R.id.favorite_synopsis_detail_tv)
    TextView mSynopsis;
    @BindView(R.id.favorite_release_date_tv)
    TextView mReleaseDate;
    /* The URI that is used to access the chosen Movie's details */
    private Uri mUri;
    private int mApiID;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);
        ButterKnife.bind(this);

        mUri = getIntent().getData();

        if (mUri == null)
            throw new NullPointerException("URI for FavoriteDetailActivity cannot be null");

        /* This connects our Activity into the loader lifecycle. */
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
    }

    /**
     * Creates and returns a CursorLoader that loads the data for our URI and stores it in a Cursor.
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

        switch (loaderId) {
            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * Runs on the main thread when a load is complete.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        /*
         * If we have valid data, we want to continue on to bind that data to the UI. If we don't
         * have any data to bind, we just return from this method.
         */
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        // Store the API ID as a Member Variable
        mApiID = data.getInt(INDEX_MOVIE_API_ID);

        // Get and set the Movie Title (store also title as Member Variable)
        mTitle = data.getString(INDEX_MOVIE_TITLE);
        setTitle(mTitle);

        // Plot Synopsis
        String synopsis = data.getString(INDEX_MOVIE_SYNOPSIS);
        mSynopsis.setText(synopsis);

        // Movie Poster Image
        String imageThumbnail = data.getString(INDEX_MOVIE_IMAGE);
        String imageThumbnailPath = APIUtils.BASE_IMAGE_PATH + imageThumbnail;

        Picasso.with(this)
                .load(imageThumbnailPath)
                .into(mPosterImage);

        // Content Description for the Movie Poster Image
        String imageContentDescription = mTitle + getString(R.string.poster_image_cd);
        mPosterImage.setContentDescription(imageContentDescription);

        // User Rating
        String userRating = data.getString(INDEX_MOVIE_RATING);
        // Set the TextView for User Rating adding the "/10"
        String userTextRating = userRating + getString(R.string.rating_ten);
        mUserRating.setText(userTextRating);

         /* Convert the userRating String to float
           and set the Rating Bar */
        float ratingBar = MovieUtils.movieRating(userRating);
        mRatingBar.setRating(ratingBar);

        // Get and set the ReleaseDate
        String releaseDate = data.getString(INDEX_MOVIE_RELEASE_DATE);
        mReleaseDate.setText(releaseDate);

        // Set the Favorite Icon inside FAB
        fab.setImageResource(R.drawable.ic_favorite_fab_icon);
    }

    /**
     * Called when a previously created loader is being reset
     */
    @NonNull
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        String deleteDialogMsg = String.format(getResources().getString(R.string.delete_dialog_message), mTitle);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(deleteDialogMsg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the products.
                deleteMovieFromDB();
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

    /**
     * Perform the deletion of the product in the database.
     * Passing the Movie API ID (not the _ID of our table)
     */
    private void deleteMovieFromDB() {

        Uri uri = MovieContract.BASE_CONTENT_URI.buildUpon()
                .appendPath(MovieContract.PATH_MOVIES)
                .appendPath(Integer.toString(mApiID))
                .build();

        // Delete a single row of data using a ContentResolver
        int rowsDeleted = getContentResolver().delete(uri, null, null);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, "The Deletion Failed",
                    Toast.LENGTH_SHORT).show();
        } else { //The Movie deleted from the database
            String delete_movie_message = String.format(getResources().getString(R.string.delete_favorite_movie_message), mTitle);
            Toast.makeText(FavoriteDetailActivity.this, delete_movie_message, Toast.LENGTH_SHORT).show();
        }
        // This Method finish() and restarts the activity
//        Intent intent = new Intent(this, MainActivity.class);
        finish();
//        startActivity(intent);
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
