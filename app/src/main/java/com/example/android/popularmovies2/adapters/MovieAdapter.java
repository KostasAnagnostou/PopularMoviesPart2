package com.example.android.popularmovies2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies2.R;
import com.example.android.popularmovies2.models.Movie;
import com.example.android.popularmovies2.utilities.APIUtils;
import com.example.android.popularmovies2.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link MovieAdapter} Renders a list of movies
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    final private MovieAdapterOnClickHandler mOnClickHandler;
    private Context mContext;
    private List<Movie> mMovies;

    /**
     * Constructor for MovieAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     */
    public MovieAdapter(@NonNull Context context, List<Movie> movies, MovieAdapterOnClickHandler onClickHandler) {
        mContext = context;
        mMovies = movies;
        mOnClickHandler = onClickHandler;
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
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);

        return new MovieViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        Movie movie = mMovies.get(position);

        String title = movie.getTitle();
        String image = movie.getThumbnail();
        String imageUrl = APIUtils.BASE_IMAGE_PATH + image;
        String ratingText = movie.getUserRating();
        float rating = MovieUtils.movieRating(ratingText);
        String id = movie.getApiId();

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
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    /**
     * When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param ,movies the new cursor to use as ForecastAdapter's data source
     */
    public void updateMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    // Interface to define which item clicked
    public interface MovieAdapterOnClickHandler {
        void onClick(int clickedMovieIndex);
    }

    /**
     * Cache of the children views for a list item.
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        public MovieViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.movie_title_text_view);
            thumbnailImageView = itemView.findViewById(R.id.movie_poster_thumbnail_image);
            ratingBar = itemView.findViewById(R.id.user_rating_rb);
            ratingTextView = itemView.findViewById(R.id.user_rating_tv);

            // Call setOnClickListener on the View passed into the constructor
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickHandler.onClick(clickedPosition);
        }
    }
}