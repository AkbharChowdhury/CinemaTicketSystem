package classes;

public class ShowTimes {
    private int showTimeID;
    private String showDate;
    private String showTime;

    public ShowTimes(){

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

    public ShowTimes(int showTimeID, String showDate, String showTime) {
        this.showTimeID = showTimeID;
        this.showDate = showDate;
        this.showTime = showTime;
    }
}
