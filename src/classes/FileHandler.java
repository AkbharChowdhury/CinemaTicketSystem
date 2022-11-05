package classes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static List<Movie> getMovieData(String fileName) throws FileNotFoundException {

        List<Movie> movieList = new ArrayList<>();

        for (String line : getCSVFileDetails(fileName)) {
            String[] values = line.split(",");
            String title = values[0];
            int duration = Integer.parseInt(values[1]);
            int rating = Integer.parseInt(values[2]);

            movieList.add(new Movie(title, duration, rating));

        }
        return movieList;
    }



    // reads the first line in the csv file
    public static List<String> readSingleColumn(String fileName) throws FileNotFoundException {
        return new ArrayList<>(getCSVFileDetails(fileName));
    }

    private static  List<String> getCSVFileDetails(String filename) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filename))
                .lines()
                .skip(1).toList();
    }
}
