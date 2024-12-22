package classes.models;

import enums.FormDetails;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
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
    private final Predicate<MovieGenres> filterTitle = p -> StringUtils.containsIgnoreCase(p.getTitle(), title);

    private Predicate<MovieGenres> filterGenre() {
        return !FormDetails.defaultGenre().equals(genre) ? p -> StringUtils.containsIgnoreCase(p.getGenres(), genre) : p -> true;
    }

    public SearchMovie(List<MovieGenres> LIST) {
        list = Collections.unmodifiableList(LIST);
    }


    public List<MovieGenres> filterResults() {
        return list.stream()
                .filter(filterTitle)
                .filter(filterGenre())
                .toList();
    }

    @Override
    public String toString() {
        return list.stream().toList().toString();
    }
}