package classes;

public class MovieGenres {
    private int movieID;
    private int genreID;
    public MovieGenres(){

    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public MovieGenres(int movieID, int genreID) {
        this.movieID = movieID;
        this.genreID = genreID;
    }



}
