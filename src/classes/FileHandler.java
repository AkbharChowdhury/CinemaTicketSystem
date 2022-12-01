package classes;

import enums.Files;

import java.io.*;
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

    public static List<Customer> getCustomerData() throws FileNotFoundException {

        List<Customer> customerList = new ArrayList<>();

        for (String line : getCSVFileDetails(Helper.getCSVPath() + Files.Customers.DESCRIPTION)) {
            String[] values = line.split(",");
            String firstname = values[0];
            String lastname = values[1];
            String email = values[2];
            String password =  Encryption.encode(values[3]);
            int ticketID = Integer.parseInt(values[4]);

            customerList.add(new Customer(firstname, lastname, email,password, ticketID));

        }
        return customerList;
    }

    public static List<ShowTimes> getShowTimeData() throws FileNotFoundException {
        String fileName = Helper.getCSVPath() + Files.ShowTimes.DESCRIPTION;
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

    public static List<MovieShowTimes> getMovieShowTimesData(String fileName) throws FileNotFoundException {

        List<MovieShowTimes> movieShowTimesList = new ArrayList<>();

        for (String line : getCSVFileDetails(fileName)) {
            String[] values = line.split(",");
            // note that the zero index is ignored
            int movieID = Integer.parseInt(values[1]);
            int showTimeID = Integer.parseInt(values[2]);
            int numTicketsLeft = Integer.parseInt(values[3]);
            movieShowTimesList.add(new MovieShowTimes(movieID, showTimeID, numTicketsLeft));

        }
        return movieShowTimesList;
    }

//    public static List<MovieShowTimes> getMovieShowTimesDatas() throws FileNotFoundException{
//        List<MovieShowTimes> movieShowTimesList = new ArrayList<>();
//
//        String line;
//        try (BufferedReader br = new BufferedReader(new FileReader(Helper.getCSVPath() + Files.MovieShowTimes.DESCRIPTION))) {
//            while (( line = br.readLine()) != null) {
//                String[] data = line.split(",");
//                int movieID = Integer.parseInt(data[1]);
//                int showTimeID = Integer.parseInt(data[2]);
//                int numTicketsLeft = Integer.parseInt(data[3]);
//                movieShowTimesList.add(new MovieShowTimes(movieID, showTimeID, numTicketsLeft));
//
//            }
//        } catch (Exception e) {
//           e.printStackTrace();
//        }
//        return movieShowTimesList;
//    }




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
    public static boolean printInvoice(String output){

        try {
              new FileWriter("invoice.txt").write(output);
            return true;


        } catch (Exception e){
            return false;

        }
//        try(var myWriter = new FileWriter("invoice.txt")) {
//            myWriter.write(output);
//            return true;
//        } catch (IOException e) {
//            System.err.println("An error occurred.");
//            e.printStackTrace();
//            return false;
//
//        }

    }
}
