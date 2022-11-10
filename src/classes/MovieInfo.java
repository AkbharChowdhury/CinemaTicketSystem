package classes;

public final class MovieInfo {
    private static int movieID;

    public static int getMovieID() {
        return movieID;
    }

    public static void setMovieID(int movieID) {
        MovieInfo.movieID = movieID;
    }

    private MovieInfo(){

    }
}
