import classes.Database;
import classes.FileHandler;
import classes.Helper;
import classes.Ticket;
import enums.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Database db;
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        db = Database.getInstance();
        List<Ticket> ticketList = FileHandler.getTicketData(Helper.getCSVPath() + Files.Tickets.DESCRIPTION);


        for(var t : ticketList){
            System.out.println(t.getPrice());;
        }

    }
}
