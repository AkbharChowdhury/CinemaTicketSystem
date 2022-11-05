package classes;

public class Ratings {
    private int ratingID;
    private String rating;
    public Ratings(){

    }

    public int getRatingID() {
        return ratingID;
    }

    public void setRatingID(int ratingID) {
        this.ratingID = ratingID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Ratings(int ratingID, String rating) {
        this.ratingID = ratingID;
        this.rating = rating;
    }
}
