package com.example.android.popularmovies2.utilities;

/**
 * Class containing utility methods
 */

public class MovieUtils {

    // This Method calculates the Movie Rating for Rating Bar
    public static float movieRating(String rating) {

        float movieRating = Float.parseFloat(rating);

        if (movieRating >= 0 && movieRating <= 2.2) {
            movieRating = 1.0f;
        } else if (movieRating > 2.3 && movieRating < 2.7) {
            movieRating = 1.5f;
        } else if (movieRating <= 4.2) {
            movieRating = 2.0f;
        } else if (movieRating > 4.3 && movieRating < 4.7) {
            movieRating = 2.5f;
        } else if (movieRating <= 6.2) {
            movieRating = 3.0f;
        } else if (movieRating > 6.3 && movieRating < 6.7) {
            movieRating = 3.5f;
        } else if (movieRating <= 8.2) {
            movieRating = 4.0f;
        } else if (movieRating > 8.3 && movieRating < 9.2) {
            movieRating = 4.5f;
        } else {
            movieRating = 5.0f;
        }

        return movieRating;
    }
}
