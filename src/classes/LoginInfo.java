package classes;

public final class LoginInfo {
    private static int customerID;

    public static int getCustomerID() {
        return customerID;
    }

    public static void setCustomerID(int customerID) {
        LoginInfo.customerID = customerID;
    }

    private LoginInfo(){

    }
}
