import classes.Helper;
import classes.Movie;

import enums.Files;
import classes.FileHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class F {
    public static final String delimiter = ",";

    //    public static void read(String csvFile) {
//        try {
//            File file = new File(csvFile);
//            FileReader fr = new FileReader(file);
//            BufferedReader br = new BufferedReader(fr);
//            String line = "";
//            String[] tempArr;
//            while((line = br.readLine()) != null) {
//                tempArr = line.split(delimiter);
//                for(String tempStr : tempArr) {
//                    System.out.print(tempStr + " ");
//                }
//                System.out.println();
//            }
//            br.close();
//        } catch(IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }
    public static void main(String[] args) throws FileNotFoundException {
//        FileHandler.getMovieData(Helper.getCSVPath() + Files.Movies.DESCRIPTION);
        String movieFile = Helper.getCSVPath() + Files.Movies.DESCRIPTION;
        var movies = FileHandler.getMovieData(movieFile);
        for (var movie : movies) {
            System.out.println(movie.getTitle());
        }


        // csv file to read
//        F.read(moviesFile.toString());


//        System.out.println(xmlLines.size());
    }


//    static void funcCSV() throws FileNotFoundException {
//        List<String> xmlLines = new BufferedReader(new FileReader(moviesFile.toString()))
//                .lines()
//                .skip(2).toList();
//
//        for(var line : xmlLines){
//            String[] values = line.split(",");
//            String title = values[0];
//            String duration = values[1];
//            String rating = values[2];
//
//
//            System.out.println(duration);
//        }
//    }
}