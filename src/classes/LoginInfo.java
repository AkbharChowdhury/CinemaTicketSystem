package classes;

public final class LoginInfo {
    private static int customerID;
    private static String email;
    private static boolean hasOpenFormOnStartUp;


    public static boolean hasOpenFormOnStartUp() {
        return hasOpenFormOnStartUp;
    }

    public static void setHasOpenFormOnStartUp(boolean status) {
        hasOpenFormOnStartUp = status;
    }


    public static String getEmail(){
        return email;
    }
    public static void setEmail(String email){
       LoginInfo.email = email;
    }



    public static int getCustomerID() {
        return customerID;
    }

    public static void setCustomerID(int id) {
        customerID = id;
    }

    private LoginInfo(){

    }
}
