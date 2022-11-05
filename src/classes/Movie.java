package classes;

public class Movie {
    private int movieID;
    private String title;
    private double duration;
    private int ratingID;

    public Movie(){

    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getRatingID() {
        return ratingID;
    }

    public void setRatingID(int ratingID) {
        this.ratingID = ratingID;
    }

    public Movie(String title, double duration, int ratingID) {
        this.title = title;
        this.duration = duration;
        this.ratingID = ratingID;
    }


}
