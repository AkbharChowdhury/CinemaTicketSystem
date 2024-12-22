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

}
