public class Main {
    public static void main(String[] args) {
        String s ="Jons";
        if (!replaceSpecialChar(s).isEmpty()){
            System.out.println(replaceSpecialChar(s));
        } else {
            System.out.println("empty");
        }


//        System.out.println(str.replace('\'', ' '));

    }
    static String replaceSpecialChar(String str){
        var arr = str.split(" ");
        String first = arr[0];
        String subString = "'";
        if (first.contains(subString)){
            return first.replace('\'', ' ');

        } return "";

    }

}
