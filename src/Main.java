import classes.*;
import enums.Files;

import java.io.*;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Database db;

    public static void main(String[] args) throws SQLException, IOException {
        db = Database.getInstance();
    }
}
