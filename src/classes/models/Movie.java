package classes.models;

import enums.FormDetails;
import interfaces.Queries;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import tables.MovieTable;
import tables.RatingTable;

import javax.swing.*;
@Data
public class Movie implements Queries {
    protected int movieID;
    protected String title = "";
    protected int duration;
    protected int ratingID;


    public Movie() {

    }

    public static void movieComboBoxStatus(JComboBox<String> cbMovies) {
        if (((DefaultComboBoxModel<String>) cbMovies.getModel()).getIndexOf(FormDetails.defaultMovie()) != -1) {
            cbMovies.removeItemAt(0);
        }
    }



    public Movie(String title, int duration, int ratingID) {
        this.title = title;
        this.duration = duration;
        this.ratingID = ratingID;
    }



    public Movie(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    public Movie(int movieID) {
        this.movieID = movieID;
    }


    public Movie(int movieID, String title) {
        this.movieID = movieID;
        this.title = title;
    }

    public Movie(int movieID, String title, int duration) {
        this.movieID = movieID;
        this.title = title;
        this.duration = duration;
    }

    @Override
    public String createTable() {
        return STR. """
                        CREATE TABLE IF NOT EXISTS \{ MovieTable.TABLE_NAME } (
                            \{ MovieTable.COLUMN_ID } INTEGER PRIMARY KEY AUTOINCREMENT,
                            \{ MovieTable.COLUMN_TITLE } TEXT NOT NULL UNIQUE,
                            \{ MovieTable.COLUMN_DURATION } TEXT NOT NULL,
                            \{ MovieTable.COLUMN_RATING_ID } INTEGER NOT NULL,
                            FOREIGN KEY(\{ MovieTable.COLUMN_RATING_ID }) REFERENCES \{ RatingTable.TABLE_NAME }(\{ RatingTable.COLUMN_ID }) ON UPDATE CASCADE ON DELETE CASCADE
                        );
                        """ ;
    }

    @Override
    public String insert() {
        return STR. """
                        INSERT INTO \{ MovieTable.TABLE_NAME }
                        VALUES (?, ?, ?, ?);
                        """ ;


    }
}
