package com.example.android.popularmovies2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies2.R;
import com.example.android.popularmovies2.models.MovieReview;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();
    final private ReviewAdapterOnClickHandler mOnClickHandler;
    private Context mContext;
    private List<MovieReview> mReviews;

    /**
     * Create a new {@link ReviewAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param reviews is the list of {@link MovieReview}'s to be displayed.     *
     */
    public ReviewAdapter(@NonNull Context context, List<MovieReview> reviews, ReviewAdapter.ReviewAdapterOnClickHandler onClickHandler) {
        mContext = context;
        mReviews = reviews;
        mOnClickHandler = onClickHandler;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);

        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {

        // Gets the MovieReview Object at the appropriate position
        MovieReview review = mReviews.get(position);

        // Get the author from the MovieReview object
        String author = review.getAuthor();
        // Get the review from the MovieReview object
        String reviewContent = review.getContent();

        // Set the author of the current MovieReview in that TextView
        holder.authorTextView.setText(author);

        // Set the review content of the current MovieReview in that TextView
        holder.reviewTextView.setText(reviewContent);
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    /**
     * Updates the data (reviews) - we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param reviews the new List to use as data source
     */
    public void loadReviews(List<MovieReview> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    // Interface to define which item clicked
    public interface ReviewAdapterOnClickHandler {
        void onClickReview(int clickedReviewIndex);
    }

    /**
     * Cache of the children views for a list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView authorTextView;
        final TextView reviewTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link VideoAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public ViewHolder(View itemView) {
            super(itemView);

            authorTextView = itemView.findViewById(R.id.author_name);
            reviewTextView = itemView.findViewById(R.id.content_review);

            // Call setOnClickListener on the View passed into the constructor
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickHandler.onClickReview(clickedPosition);
        }
    }
}