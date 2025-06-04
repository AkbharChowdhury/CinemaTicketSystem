package classes;

import enums.RedirectPage;
import lombok.Getter;
import lombok.Setter;

public final class Form {

    @Setter
    @Getter
    private static RedirectPage redirectPage;

    private Form() {
    }

    public static RedirectPage getRedirectPage() {
        return redirectPage;
    }

    public static void setRedirectPage(RedirectPage redirectPage) {
        Form.redirectPage = redirectPage;
    }
}
