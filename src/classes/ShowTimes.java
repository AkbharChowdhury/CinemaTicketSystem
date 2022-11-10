package classes;

import interfaces.Queries;
import tables.MovieTable;
import tables.RatingTable;
import tables.ShowTimesTable;

public class ShowTimes implements Queries {
    private int showTimeID;
    private String showDate;
    private String showTime;

    public ShowTimes() {

    }

    public ShowTimes(int showTimeID, String showDate, String showTime) {
        this.showTimeID = showTimeID;
        this.showDate = showDate;
        this.showTime = showTime;
    }

    public ShowTimes(String showDate, String showTime) {
        this.showDate = showDate;
        this.showTime = showTime;

    }



    public int getShowTimeID() {
        return showTimeID;
    }

    public void setShowTimeID(int showTimeID) {
        this.showTimeID = showTimeID;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

//    @Override
//    public String createTable() {
//        return String.format("""
//                        CREATE TABLE IF NOT EXISTS %s (
//                        %s INTEGER PRIMARY KEY AUTOINCREMENT,
//                        %s TEXT NOT NULL,
//                        %s TEXT NOT NULL
//                        );
//                        """,
//                ShowTimesTable.TABLE_NAME,
//                ShowTimesTable.COLUMN_ID,
//                ShowTimesTable.COLUMN_DATE,
//                ShowTimesTable.COLUMN_TIME
//
//
//        );
//    }

    @Override
    public String createTable() {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                        %s INTEGER PRIMARY KEY AUTOINCREMENT,
                        %s TEXT NOT NULL,
                        %s TEXT NOT NULL
                        );
                        """,
                ShowTimesTable.TABLE_NAME,
                ShowTimesTable.COLUMN_ID,
                ShowTimesTable.COLUMN_DATE,
                ShowTimesTable.COLUMN_TIME


        );
    }

    @Override
    public String insert() {
        return String.format("""
                        INSERT INTO %s
                        VALUES (?, ?, ?);
                        """,
                ShowTimesTable.TABLE_NAME
        );
    }
}
