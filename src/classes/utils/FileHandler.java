package classes.utils;

import classes.models.*;
import enums.Files;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private FileHandler(){}
    public static final String CSV_PATH = "src/csv/";

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

    public static List<Customer> getCustomerData() throws FileNotFoundException {

        List<Customer> customerList = new ArrayList<>();

        for (String line : getCSVFileDetails(CSV_PATH + Files.Customers.DESCRIPTION)) {
            String[] values = line.split(",");
            String firstname = values[0];
            String lastname = values[1];
            String email = values[2];
            String password = Encryption.encode(values[3]);
            int ticketID = Integer.parseInt(values[4]);

            customerList.add(new Customer(firstname, lastname, email, password, ticketID));

        }
        return customerList;
    }

    public static List<ShowTimes> getShowTimeData() throws FileNotFoundException {
        String fileName = CSV_PATH + Files.ShowTimes.DESCRIPTION;
        List<ShowTimes> showTimeList = new ArrayList<>();

        for (String line : getCSVFileDetails(fileName)) {
            String[] values = line.split(",");
            // note that if the first column contains a integer this won't work
            int movieID = Integer.parseInt(values[1]);
            String date = values[2];
            String time = values[3];
            int numTickets = Integer.parseInt(values[4]);


            var showTime = new ShowTimes();
            showTime.setMovieID(movieID);
            showTime.setDate(date);
            showTime.setTime(time);
            showTime.setNumTicketsLeft(numTickets);

            showTimeList.add(showTime);

        }
        return showTimeList;
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
    private static List<String> readSingleColumn(String filename) throws FileNotFoundException {
        return new ArrayList<>(getCSVFileDetails(filename));
    }

    private static List<String> getCSVFileDetails(String filename) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filename))
                .lines()
                .toList();
    }


    public static boolean printInvoice(String output) {

        try (var file = new FileWriter(Invoice.INVOICE_FILE)) {
            file.write(output);
            return true;
        } catch (Exception e) {
            return false;

        }


    }


    public static List<String> readSingleLineCSV(String filename) throws FileNotFoundException {
        return readSingleColumn(FileHandler.CSV_PATH + filename);
    }
}
