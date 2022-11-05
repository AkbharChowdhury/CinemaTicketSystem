package classes;

public class Sales {
    private int salesID;
    private String salesDate;
    private int customerID;

    public Sales(){

    }


    public Sales(int salesID, String salesDate, int customerID) {
        this.salesID = salesID;
        this.salesDate = salesDate;
        this.customerID = customerID;
    }

    public int getSalesID() {
        return salesID;
    }

    public void setSalesID(int salesID) {
        this.salesID = salesID;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
}
