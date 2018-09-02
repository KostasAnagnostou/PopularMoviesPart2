package com.example.android.popularmovies2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies2.adapters.MovieAdapter;
import com.example.android.popularmovies2.adapters.MovieCursorAdapter;
import com.example.android.popularmovies2.data.MovieContract;
import com.example.android.popularmovies2.models.Movie;
import com.example.android.popularmovies2.models.MoviesResults;
import com.example.android.popularmovies2.retrofit.APIServiceInterface;
import com.example.android.popularmovies2.utilities.APIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>,
        MovieCursorAdapter.MovieCursorAdapterOnClickHandler {

    public static final int MOVIE_LOADER_ID = 0;
    // The columns to return for each row on Movies Table
    public static final String[] FAVORITE_MOVIE_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_API_ID
    };
    /**
     * We store the indices of the values in the array of Strings above to be able to
     * access the data from our query more quickly - used in MovieCursorAdapter
     */
    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_SYNOPSIS = 2;
    public static final int INDEX_MOVIE_RATING = 3;
    public static final int INDEX_MOVIE_IMAGE = 4;
    public static final int INDEX_MOVIE_RELEASE_DATE = 5;
    public static final int INDEX_MOVIE_API_ID = 6;
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String RECYCLER_POSITION_STATE = "recycler_position";
    private static final String TOP_RATED_STATE = "top_rated_state";
    private static final String FAVORITE_STATE = "favorite_state";
    // Create and bind the Views with butter knife library
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;
    @BindView(R.id.no_internet_icon)
    ImageView noInternetIcon;
    @BindView(R.id.empty_view)
    TextView mEmptyStateTextView;
    @BindView(R.id.gridview_movies_layout)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_favorite_movie_image)
    ImageView mEmptyFavoriteListIcon;
    @BindView(R.id.favorite_empty_text_view)
    TextView mEmptyFavoriteListTextView;
    private List<Movie> mMovies;
    private MovieAdapter mAdapter;
    private MovieCursorAdapter mCursorAdapter;
    private APIServiceInterface mAPIService;
    private GridLayoutManager gridLayoutManager;
    private Parcelable saveRecyclerPosition;
    private boolean isTopRated;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAPIService = APIUtils.getAPIInterface();
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>(0), this);
        mCursorAdapter = new MovieCursorAdapter(this, this);

        // Portrait Mode
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // A GridLayoutManager with default vertical orientation and 2 number of columns
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        }
        // Landscape Mode
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // A GridLayoutManager with default vertical orientation and 2 number of columns
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        }

        // If the Activity Recreated
        if (savedInstanceState != null) {
            // store the saved states/variables(onSaveInstanceState()) and Recycler position
            isTopRated = savedInstanceState.getBoolean(TOP_RATED_STATE);
            isFavorite = savedInstanceState.getBoolean(FAVORITE_STATE);
            saveRecyclerPosition = savedInstanceState.getParcelable(RECYCLER_POSITION_STATE);
            gridLayoutManager.onRestoreInstanceState(saveRecyclerPosition);
            if (isTopRated) {
                fetchTopRatedMovies();
            } else if (isFavorite) {
                fetchFavoriteMovies();
            } else {
                fetchPopularMovies();
            }
        } else {// Default: Fetch Popular Movies
            showLoadingIndicator();
            fetchPopularMovies();
        }

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    // This Method fetches the Popular Movies - (Async Call with Retrofit2)
    // used example from here --> https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792
    private void fetchPopularMovies() {
        showLoadingIndicator();
        mAPIService.getPopularMovies().enqueue(new Callback<MoviesResults>() {
            @Override
            public void onResponse(Call<MoviesResults> call, Response<MoviesResults> response) {
                if (response.isSuccessful()) {
                    setTitle(R.string.popular_label);
                    showMovieRecyclerView();
                    // Fetch the Popular Movies
                    mMovies = response.body().getResults();
                    mAdapter.updateMovies(mMovies);
                    // Set the adapter on the RecyclerView, so the list can be populated in the UI
                    mRecyclerView.setAdapter(mAdapter);
                    // return states
                    isTopRated = false;
                    isFavorite = false;
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    Log.d(LOG_TAG, "ERROR with the API Response " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MoviesResults> call, Throwable t) {
                // something went wrong (no internet connection, etc)
                showNoInternetMessage();
                Log.d("Error", t.getMessage());
            }
        });
    }

    // This Method Fetches the Top Rated Movies
    private void fetchTopRatedMovies() {
        showLoadingIndicator();
        mAPIService.getTopRatedMovies().enqueue(new Callback<MoviesResults>() {
            @Override
            public void onResponse(Call<MoviesResults> call, Response<MoviesResults> response) {
                if (response.isSuccessful()) {
                    setTitle(R.string.top_rated_label);
                    showMovieRecyclerView();
                    // Fetch the Popular Movies
                    mMovies = response.body().getResults();
                    mAdapter.updateMovies(mMovies);
                    // Set the adapter on the RecyclerView, so the list can be populated in the UI
                    mRecyclerView.setAdapter(mAdapter);
                    // return states
                    isTopRated = true;
                    isFavorite = false;
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    Log.d(LOG_TAG, "ERROR with the API Response " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MoviesResults> call, Throwable t) {
                // something went wrong (no internet connection, etc)
                showNoInternetMessage();
                Log.d("Error", t.getMessage());
            }
        });
    }

    // This Method fetches the favorite Movies
    // From our Movies Table
    private void fetchFavoriteMovies() {
        setTitle(R.string.favorites_label);
        // Attach the Adapter to the RecyclerView
        mRecyclerView.setAdapter(mCursorAdapter);
        /* initialize Loader */
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    /*
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return movie data as a Cursor or null if an error occurs.
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle loaderArgs) {
        switch (loaderId) {
            case MOVIE_LOADER_ID:

                /* URI for all rows of movie data in our movie table */
                Uri queryUri = MovieContract.MovieEntry.CONTENT_URI;
                /* Sort order by ID */
                String sortOrder = MovieContract.MovieEntry._ID;

                return new CursorLoader(this,
                        queryUri,
                        FAVORITE_MOVIE_PROJECTION,
                        null,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * Called when a Loader has finished loading its data.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        // return states
        isFavorite = true;
        isTopRated = false;
        if (data.getCount() != 0) {
            showMovieRecyclerView();
        } else {
            showEmptyFavoriteList();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    // This method invokes when a Popular or Top Rated Movie is clicked
    // Opens the Movie Detail Activity
    @Override
    public void onClick(int clickedMovieIndex) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie", mMovies.get(clickedMovieIndex));
        startActivity(intent);
    }

    // This method invokes when a favorite movie is clicked
    @Override
    public void onClickFavorite(int clickedId) {
        Intent intent = new Intent(MainActivity.this, FavoriteDetailActivity.class);
        Uri uriForIDClicked = MovieContract.MovieEntry.buildMovieUriWithID(clickedId);
        intent.setData(uriForIDClicked);
        startActivity(intent);
    }

    // This method holds the Recycler View layout state (position)
    // and the state of the 2 booleans
    // this example helped --> https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities/28262885
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_POSITION_STATE, gridLayoutManager.onSaveInstanceState());
        outState.putBoolean(TOP_RATED_STATE, isTopRated);
        outState.putBoolean(FAVORITE_STATE, isFavorite);
    }

    private void showMovieRecyclerView() {
        // Hide the Views For No Internet,
        // Loading Indicator & Empty Favorite List
        noInternetIcon.setVisibility(View.INVISIBLE);
        mEmptyStateTextView.setVisibility(View.INVISIBLE);
        mEmptyFavoriteListIcon.setVisibility(View.INVISIBLE);
        mEmptyFavoriteListTextView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        // Display the GridView (Display the movies)
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showEmptyFavoriteList() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyFavoriteListIcon.setVisibility(View.VISIBLE);
        mEmptyFavoriteListTextView.setVisibility(View.VISIBLE);
        noInternetIcon.setVisibility(View.INVISIBLE);
        mEmptyStateTextView.setVisibility(View.INVISIBLE);
    }

    private void showNoInternetMessage() {
        if (!isConnected()) {
            // First, hide loading indicator
            loadingIndicator.setVisibility(View.INVISIBLE);
            noInternetIcon.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyFavoriteListIcon.setVisibility(View.INVISIBLE);
            mEmptyFavoriteListTextView.setVisibility(View.INVISIBLE);
        }
    }

    // This method checks if the user has Internet connection
    private boolean isConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        // boolean to check if there is a network connection
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Methods for setting up the menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            setTitle(R.string.popular_label);
            fetchPopularMovies();
            return true;

        }
        if (id == R.id.action_top_rated) {
            setTitle(R.string.top_rated_label);
            fetchTopRatedMovies();
            return true;
        }
        if (id == R.id.action_favorite) {
            setTitle(R.string.favorites_label);
            fetchFavoriteMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}