package classes;

import enums.Files;

import java.io.FileNotFoundException;
import java.util.List;

public final class Helper {

    public static String getCSVPath(){
        return "src/csv/";
    }
    public static List<String> readSingleLineCSV(String filename) throws FileNotFoundException {
        return FileHandler.readSingleColumn(getCSVPath() + filename);
    }
    public static String calcDuration(int duration){
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format( "%d:%02d", hours, minutes);
    }





}
