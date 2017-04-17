/*In the name of Allah*/
package sani.ango.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private String movieTitle;
    private double viewerRatings;
    private String overview;
    private String releasedDate;
    private String posterImage;
    private String backPoster;
    private final static String BASE_URL = "http://image.tmdb.org/t/p/w185//";

    public Movie(String title, double ratings,
                 String summary, String date, String relativePath, String backdropPath){
        movieTitle = title;
        viewerRatings = ratings;
        overview = summary;
        releasedDate = date;
        posterImage = BASE_URL + relativePath;
        backPoster = BASE_URL + backdropPath;
    }

    protected Movie(Parcel in) {
        movieTitle = in.readString();
        viewerRatings = in.readDouble();
        overview = in.readString();
        releasedDate = in.readString();
        posterImage = in.readString();
        backPoster = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public double getViewerRatings() {
        return viewerRatings;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public String getMovieTitle() {

        return movieTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeDouble(viewerRatings);
        dest.writeString(overview);
        dest.writeString(releasedDate);
        dest.writeString(posterImage);
        dest.writeString(backPoster);
    }
}
