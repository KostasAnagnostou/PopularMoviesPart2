package com.example.android.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Movie class holds the information for a Movie
 * We implement Parcelable to be able to pass objects through activities etc
 * followed an example from here for Parcelable -> http://www.vogella.com/tutorials/AndroidParcelable/article.html
 * I used an example for retrofit from here -- > https://www.journaldev.com/13639/retrofit-android-example-tutorial
 */

public class Movie implements Parcelable {

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
    // Variables to store the info for a movie
    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("overview")
    @Expose
    private String mSynopsis;
    @SerializedName("vote_average")
    @Expose
    private String mUserRating;
    @SerializedName("poster_path")
    @Expose
    private String mThumbnail;
    @SerializedName("release_date")
    @Expose
    private String mReleaseDate;
    @SerializedName("id")
    @Expose
    private String mMovieAPIId;

    /**
     * Constructor - constructs a new {@link Movie} Object with 5 params
     *
     * @param title       is the Title of the Movie
     * @param synopsis    is the Synopsis - Small Description
     * @param thumbnail   is the Movie Poster Thumbnail Image
     * @param userRating  is the User Rating of the movie
     * @param releaseDate the Release Date of the Movie
     * @param movieAPIId  the ID of the Movie in the API Database
     */

    public Movie(String title, String synopsis, String thumbnail, String userRating, String releaseDate, String movieAPIId) {
        this.mTitle = title;
        this.mSynopsis = synopsis;
        this.mThumbnail = thumbnail;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
        this.mMovieAPIId = movieAPIId;
    }

    // Parcelling Implementation
    public Movie(Parcel in) {
        mTitle = in.readString();
        mSynopsis = in.readString();
        mThumbnail = in.readString();
        mUserRating = in.readString();
        mReleaseDate = in.readString();
        mMovieAPIId = in.readString();
    }

    // * Get (return) the Title of the Movie
    public String getTitle() {
        return mTitle;
    }

    // * Get (return) the Synopsis (Overview) of the Movie
    public String getSynopsis() {
        return mSynopsis;
    }

    // * Get (return) the Thumbnail Image of the Movie
    public String getThumbnail() {
        return mThumbnail;
    }

    // * Get (return) the User Rating of the Movie
    public String getUserRating() {
        return mUserRating;
    }

    // * Get (return) the Release Date of the Movie
    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getApiId() {
        return mMovieAPIId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mSynopsis);
        parcel.writeString(mThumbnail);
        parcel.writeString(mUserRating);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mMovieAPIId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}