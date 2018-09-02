package com.example.android.popularmovies2.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies2.R;
import com.example.android.popularmovies2.models.MovieVideo;
import com.example.android.popularmovies2.utilities.APIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();
    final private VideoAdapterOnClickHandler mOnClickHandler;
    private Context mContext;
    private List<MovieVideo> mVideos;

    /**
     * Create a new {@link VideoAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param videos  is the list of {@link MovieVideo}s to be displayed.
     */
    public VideoAdapter(@NonNull Context context, List<MovieVideo> videos, VideoAdapterOnClickHandler onClickHandler) {
        mContext = context;
        mVideos = videos;
        mOnClickHandler = onClickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.video_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Gets the Movie object from the ArrayAdapter at the appropriate position
        MovieVideo video = mVideos.get(position);

        // Get the title string from the Movie object
        String title = video.getName();
        // Get the Youtube Key for the Video
        String youtubeKey = video.getKey();

        // Get the Path for the Image Thumbnail of the movie
        String imageThumbnail = APIUtils.YOUTUBE_BASE_IMAGE_URL + youtubeKey + APIUtils.YOUTUBE_JPG_ENDING_URL;

        // Set the title of the current Movie in that TextView
        holder.titleTextView.setText(title);

        // Set the Image of the Current Movie in the ImageView
        Picasso.with(mContext)
                .load(imageThumbnail)
                .into(holder.thumbnailImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mVideos) return 0;
        return mVideos.size();
    }

    /**
     * Updates the data (videos) - we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param videos the new List to use as data source
     */
    public void updateVideos(List<MovieVideo> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    // Interface to define which item clicked
    public interface VideoAdapterOnClickHandler {
        void onClick(int clickedVideoIndex);
    }

    /**
     * Cache of the children views for a list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView titleTextView;
        final ImageView thumbnailImageView;

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

            titleTextView = itemView.findViewById(R.id.video_title);
            thumbnailImageView = itemView.findViewById(R.id.video_image_thumbnail);

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
