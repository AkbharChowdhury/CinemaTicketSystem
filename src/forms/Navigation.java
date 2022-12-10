package forms;

import bsh.EvalError;
import bsh.Interpreter;
public final class Navigation {
    public static void s(){
        System.out.println("working");
    }
    public  void  m(){

        System.out.println("m called");
    }
    public static void hello(){
        System.out.println("hello man");
    }

    private static final Interpreter interpreter = new Interpreter();

    public static void importNavigation() throws EvalError {

        interpreter.eval("""
                  Navigation nav = new Navigation();
                        nav.m();
           
                
                """);


    }
    public static void  navigationClickEvent() throws EvalError {

        interpreter.eval("""
                 public void navigationMenu(ActionEvent e) throws SQLException, FileNotFoundException, ParseException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
                               
                               
                                       if (e.getSource() == btnListMovies) {
                                           Helper.gotoForm(this, Pages.LIST_MOVIES);
                               
                                       }
                                       if (e.getSource() == btnShowTimes) {
                                           Helper.gotoForm(this, Pages.SHOW_TIMES);
                                       }
                               
                                       if (e.getSource() == btnPurchaseTicket) {
                                           if (!Helper.isCustomerLoggedIn(this, RedirectPage.PURCHASE_TICKET)) {
                                               Helper.gotoForm(this, Pages.LOGIN);
                                               return;
                                           }
                               
                                           Helper.gotoForm(this, Pages.PURCHASE_TICKET);
                               
                                       }
                               
                                       if (e.getSource() == btnShowReceipt) {
                                           Helper.gotoForm(this, Pages.SHOW_RECEIPT);
                                       }
                               
                               
                               
                                   }
                                
                                
                                
                """);
    }
}
