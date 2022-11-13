package classes;

import enums.RedirectPage;

public final class Form {
    public static RedirectPage getRedirectPage() {
        return redirectPage;
    }

    public static void setRedirectPage(RedirectPage page) {
        redirectPage = page;
    }

    private static RedirectPage redirectPage;



    private Form(){

    }

}
