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


    public static List<MovieGenres> getMovieGenreData(String fileName) throws FileNotFoundException {

        List<MovieGenres> movieGenreList = new ArrayList<>();

        for (String line : getCSVFileDetails(fileName)) {
            String[] values = line.split(",");
            int movieID = Integer.parseInt(values[0]);
            int genreID = Integer.parseInt(values[1]);

            movieGenreList.add(new MovieGenres(movieID, genreID));

        }
        return movieGenreList;
    }

    public static List<Ticket> getTicketData(String fileName) throws FileNotFoundException {

        List<Ticket> ticketList = new ArrayList<>();

        for (String line : getCSVFileDetails(fileName)) {
            String[] values = line.split(",");
            String type = values[0];
            double price = Double.parseDouble(values[1]);

            ticketList.add(new Ticket(type, price));

        }
        return ticketList;
    }



    // reads the first line in the csv file
    public static List<String> readSingleColumn(String filename) throws FileNotFoundException {
        return new ArrayList<>(getCSVFileDetails(filename));
    }

    private static List<String> getCSVFileDetails(String filename) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filename))
                .lines()
//                .skip(1)
                .toList();
    }
}
