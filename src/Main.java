import classes.Database;
import classes.models.SearchMovie;

void main() {
    Database db = Database.getInstance();
    SearchMovie movies = new SearchMovie(db.getMovies());
    movies.setGenre("Thriller");
    System.out.println(movies.filterResults());
//    System.out.println(db.getMovies());
}
