package classes.models;

import enums.FormDetails;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SearchMovie {

    private final List<MovieGenres> list;

    private String title = "";
    private String genre = FormDetails.defaultGenre();


    private final Predicate<MovieGenres> filterTitle = p -> p.getTitle().toLowerCase().contains(title.toLowerCase());
    private Predicate<MovieGenres> filterGenre() {
        return !FormDetails.defaultGenre().equals(genre) ? c -> c.getGenre().toLowerCase().contains(genre.toLowerCase()) : p -> true;

    }


    public SearchMovie(List<MovieGenres> list) {

        this.list = Collections.unmodifiableList(list);

    }


    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public List<MovieGenres> filterResults() {
        return list.stream()
                .filter(filterTitle)
                .filter(filterGenre())
                .collect(Collectors.toList());
    }

}