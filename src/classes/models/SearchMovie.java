package classes.models;

import enums.FormDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SearchMovie {

    @Getter
    private final List<MovieGenres> list;

    @Setter
    private String title = "";
    @Setter
    private String genre = FormDetails.defaultGenre();
    private final Predicate<MovieGenres> filterTitle = p -> p.getTitle().toLowerCase().contains(title.toLowerCase());

    private Predicate<MovieGenres> filterGenre() {
        return !FormDetails.defaultGenre().equals(genre) ? c -> c.getGenres().toLowerCase().contains(genre.toLowerCase()) : p -> true;

    }

    public SearchMovie(List<MovieGenres> LIST) {
        list = LIST;
    }


    public List<MovieGenres> filterResults() {
        return list.stream()
                .filter(filterTitle)
                .filter(filterGenre())
                .collect(Collectors.toList());
    }

}