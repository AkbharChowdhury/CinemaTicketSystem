package classes;

public final class LoginInfo {
    private static int customerID;
    private static String email;


    public static String getEmail(){
        return email;
    }
    public static void setEmail(String email){
       LoginInfo.email = email;
    }



    public static int getCustomerID() {
        return customerID;
    }

    public static void setCustomerID(int customerID) {
        LoginInfo.customerID = customerID;
    }

    private LoginInfo(){

    }
}
