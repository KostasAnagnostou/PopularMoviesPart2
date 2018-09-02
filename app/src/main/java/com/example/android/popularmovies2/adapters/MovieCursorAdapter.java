package com.example.android.popularmovies2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies2.MainActivity;
import com.example.android.popularmovies2.R;
import com.example.android.popularmovies2.utilities.APIUtils;
import com.example.android.popularmovies2.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

/**
 * {@link MovieCursorAdapter} renders a list of movies from the database
 */

public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.MovieCursorViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter.
     */
    final private MovieCursorAdapterOnClickHandler mClickHandler;
    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;

    /**
     * Constructor for the MovieCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public MovieCursorAdapter(Context mContext, MovieCursorAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If our RecyclerView has more than one type of item we
     *                  can use this viewType integer to provide a different layout.
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public MovieCursorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        int layoutIdForListItem = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);

        return new MovieCursorViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(@NonNull MovieCursorViewHolder holder, int position) {

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Get the values of the wanted data
        // indices are Constants inside MainActivity
        final int id = mCursor.getInt(MainActivity.INDEX_MOVIE_ID);
        String title = mCursor.getString(MainActivity.INDEX_MOVIE_TITLE);
        String ratingText = mCursor.getString(MainActivity.INDEX_MOVIE_RATING);
        float rating = MovieUtils.movieRating(ratingText);
        String image = mCursor.getString(MainActivity.INDEX_MOVIE_IMAGE);
        String imageUrl = APIUtils.BASE_IMAGE_PATH + image;

        // Set the values
        holder.itemView.setTag(id);
        holder.titleTextView.setText(title);
        holder.ratingTextView.setText(ratingText);
        holder.ratingBar.setRating(rating);
        Picasso.with(mContext)
                .load(imageUrl)
                .into(holder.thumbnailImageView);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * Swaps the cursor used by the MovieAdapter for its movie data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the data is reset.      *
     *
     * @param newCursor the new cursor to use as MovieCursorAdapter's data source
     */
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     * We pass as parameter the Movie ID
     */
    public interface MovieCursorAdapterOnClickHandler {
        void onClickFavorite(int clickedMovieId);
    }

    /**
     * Cache of the children views for a list item.
     */
    public class MovieCursorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView titleTextView;
        final ImageView thumbnailImageView;
        final RatingBar ratingBar;
        final TextView ratingTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link MovieAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public MovieCursorViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.movie_title_text_view);
            thumbnailImageView = itemView.findViewById(R.id.movie_poster_thumbnail_image);
            ratingBar = itemView.findViewById(R.id.user_rating_rb);
            ratingTextView = itemView.findViewById(R.id.user_rating_tv);

            // Call setOnClickListener on the View passed into the constructor
            itemView.setOnClickListener(this);
        }

        /**
         * When clicked we fetch the Movie API id that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * id.
         *
         * @param view the View that was clicked
         */
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mCursor.moveToPosition(clickedPosition);
            int id = mCursor.getInt(MainActivity.INDEX_MOVIE_ID);
            mClickHandler.onClickFavorite(id);
        }
    }
}