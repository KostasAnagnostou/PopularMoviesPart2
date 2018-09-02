# PopularMoviesPart2
This app is a Project for Udacity's Android Developer Nanodegree Program. With this app you are able to inform about Popular or Top Rated Movies from TheMovieDb.org API. 

Also, you can save your favorite movies to your favorite list.

Important: 
1) In order to get results (the Movies) from TheMovieDb.org API you will need to aquire an API_KEY from [Here](https://developers.themoviedb.org/3/getting-started/introduction).
2) Then you must enter the key [Here](https://github.com/KostasAnagnostou/PopularMoviesPart2/blob/master/app/src/main/java/com/example/android/popularmovies2/utilities/APIUtils.java) 15 line

In the 1st stage we built the core experience of our movies app:

* Present the user with a grid arrangement of movie posters upon launch.
* Allow your user to change sort order via a setting:
* The sort order can be by most popular or by highest-rated
* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
  * original title
  * movie poster image thumbnail
  * A plot synopsis (called overview in the api)
  * user rating (called vote_average in the api)
  * release date

In the 2nd and final stage, we added additional functionality to the app:

We added more information to our movie details view:

* You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
* You’ll allow users to read reviews of a selected movie.
* You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star).
* You'll create a database to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to * display their favorites collection while offline).
* You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.
