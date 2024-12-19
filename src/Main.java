import classes.Database;
import classes.models.SearchMovie;

public static void main(String[] args) {
    SearchMovie movie = new SearchMovie(Database.getInstance().getMovies());
    movie.setTitle("");
    movie.setGenre("Sci-Fi");
    movie.filterResults().forEach(i -> System.out.println(STR."\{i.getTitle()} \{i.getGenres()}"));


}

