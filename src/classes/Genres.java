package classes;

public class Genres {
    private int genreID;
    private String genre;

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Genres(int genreID, String genre) {
        this.genreID = genreID;
        this.genre = genre;
    }


}
