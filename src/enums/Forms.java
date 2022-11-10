package enums;


public enum Forms {
    Login("Customer Login"),
    Logo("Cinema Ticket Machine");

    public final String DESCRIPTION;

    Forms(String title) {
        this.DESCRIPTION = title;

    }

}
