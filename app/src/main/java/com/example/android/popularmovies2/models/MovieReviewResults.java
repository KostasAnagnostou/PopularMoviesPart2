package com.example.android.popularmovies2.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewResults implements Parcelable {

    public static final Parcelable.Creator<MovieReviewResults> CREATOR = new Parcelable.Creator<MovieReviewResults>() {
        @Override
        public MovieReviewResults createFromParcel(Parcel in) {
            return new MovieReviewResults(in);
        }

        @Override
        public MovieReviewResults[] newArray(int i) {
            return new MovieReviewResults[i];
        }
    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<MovieReview> results;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    // Parcelling Implementation
    // followed an example from here for Parcelable -> http://www.vogella.com/tutorials/AndroidParcelable/article.html
    public MovieReviewResults(Parcel in) {
        id = in.readInt();
        page = in.readInt();
        results = in.createTypedArrayList(MovieReview.CREATOR);
        totalPages = in.readInt();
        totalResults = in.readInt();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<MovieReview> getResults() {
        return results;
    }

    public void setResults(List<MovieReview> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(page);
        parcel.writeList(results);
        parcel.writeInt(totalPages);
        parcel.writeInt(totalResults);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}