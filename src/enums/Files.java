package enums;

public enum Files {
    Movies("movies.csv"),
    Ratings("ratings.csv");

    public final String DESCRIPTION;
    Files(String filename){
        this.DESCRIPTION = filename;

    }

}