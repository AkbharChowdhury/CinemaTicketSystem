package classes;

import lombok.Getter;
import lombok.Setter;

public final class LoginInfo {
    public static int getCustomerID() {
        return customerID;
    }

    public static String getEmail() {
        return email;
    }

    @Getter
    @Setter
    private static int customerID;
    @Getter
    private static String email;
    @Setter
    private static boolean hasOpenFormOnStartUp;


    public static boolean hasOpenFormOnStartUp() {
        return hasOpenFormOnStartUp;
    }


    public static void setEmail(String email){
       LoginInfo.email = email;
    }


    private LoginInfo(){

    }
}
