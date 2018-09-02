package com.example.android.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideosResults implements Parcelable {

    public static final Parcelable.Creator<MovieVideosResults> CREATOR = new Parcelable.Creator<MovieVideosResults>() {
        @Override
        public MovieVideosResults createFromParcel(Parcel in) {
            return new MovieVideosResults(in);
        }

        @Override
        public MovieVideosResults[] newArray(int i) {
            return new MovieVideosResults[i];
        }
    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieVideo> results;

    // Parcelling Implementation
    // followed an example from here for Parcelable -> http://www.vogella.com/tutorials/AndroidParcelable/article.html
    public MovieVideosResults(Parcel in) {
        id = in.readInt();
        results = in.createTypedArrayList(MovieVideo.CREATOR);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieVideo> getResults() {
        return results;
    }

    public void setResults(List<MovieVideo> results) {
        this.results = results;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
