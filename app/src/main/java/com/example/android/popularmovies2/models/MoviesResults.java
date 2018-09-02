package com.example.android.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// I used an example for retrofit from here -- > https://www.journaldev.com/13639/retrofit-android-example-tutorial

public class MoviesResults implements Parcelable {
    public static final Parcelable.Creator<MoviesResults> CREATOR = new Parcelable.Creator<MoviesResults>() {
        @Override
        public MoviesResults createFromParcel(Parcel in) {
            return new MoviesResults(in);
        }

        @Override
        public MoviesResults[] newArray(int i) {
            return new MoviesResults[i];
        }
    };
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<Movie> results;


    // Parcelling Implementation
    // followed an example from here for Parcelable -> http://www.vogella.com/tutorials/AndroidParcelable/article.html
    public MoviesResults(Parcel in) {
        page = in.readInt();
        totalResults = in.readInt();
        totalPages = in.readInt();
        results = in.createTypedArrayList(Movie.CREATOR);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(page);
        parcel.writeInt(totalResults);
        parcel.writeInt(totalPages);
        parcel.writeList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

