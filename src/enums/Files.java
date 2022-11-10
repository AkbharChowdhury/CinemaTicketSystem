package enums;

public enum Files {
    Movies("movies.csv"),
    MovieGenres("movieGenres.csv"),

    Ratings("ratings.csv"),
    Genres("genres.csv"),
    Tickets("tickets.csv"),
    ShowTimes("showTimes.csv"),
    MovieShowTimes("movieShowTimes.csv"),
    Customers("customer.csv");


    public final String DESCRIPTION;

    Files(String filename) {
        this.DESCRIPTION = filename;

    }

}