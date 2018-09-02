package com.example.android.popularmovies2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    /**
     * The "Content authority" is a name for the entire content provider.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies2";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     */
    public static final String PATH_MOVIES = "movies";

    // Empty private constructor
    private MovieContract() {
    }

    /* MovieEntry is an inner class that defines the contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        /**
         * The content URI to access the movie data in the provider
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        /**
         * Name of database table for movies
         */
        public static final String TABLE_NAME = "movies";

        /*
         * Unique ID number for the product (only for use in the database table). *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Title of the Movie
         * Type: TEXT
         */
        public static final String COLUMN_TITLE = "title";

        /**
         * Synopsis of the Movie
         * Type: TEXT
         */
        public static final String COLUMN_SYNOPSIS = "synopsis";

        /**
         * Rating of the Movie
         * Type: TEXT
         */
        public static final String COLUMN_RATING = "rating";

        /**
         * Product Image
         * Type: TEXT
         */
        public final static String COLUMN_MOVIE_IMAGE = "image";

        /**
         * The Release Date of the Movie
         * Type: INTEGER
         */
        public final static String COLUMN_RELEASE_DATE = "release_date";

        /**
         * The ID of the Movie In the Movie Database API
         * Type: INTEGER
         */
        public final static String COLUMN_MOVIE_API_ID = "movie_id";

        /**
         * Builds a URI that adds the ID to the end of the content URI path.
         * This is used to query details about a single movie entry by ID.
         *
         * @param id is the Movie _ID
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUriWithID(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of movies
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single movie
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
    }
}
