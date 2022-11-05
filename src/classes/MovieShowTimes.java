package classes;

public class MovieShowTimes {
    private int movieId;
    private int showTimeID;
    private int numTicketLeft;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getShowTimeID() {
        return showTimeID;
    }

    public void setShowTimeID(int showTimeID) {
        this.showTimeID = showTimeID;
    }

    public int getNumTicketLeft() {
        return numTicketLeft;
    }

    public void setNumTicketLeft(int numTicketLeft) {
        this.numTicketLeft = numTicketLeft;
    }

    public MovieShowTimes(int movieId, int showTimeID, int numTicketLeft) {
        this.movieId = movieId;
        this.showTimeID = showTimeID;
        this.numTicketLeft = numTicketLeft;
    }


}
