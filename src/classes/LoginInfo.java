package classes;

import lombok.Getter;
import lombok.Setter;

public final class LoginInfo {
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
