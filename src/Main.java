public class Main {
    public static void main(String[] args) {
//        var query = "item's";
//        if (query.i)
//        var s = query.replace("'", "\'");
//        System.out.println(s);
        System.out.println(escapeMetaCharacters("Don't drink the wa"));


    }
    public static String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","%"};

        for (String metaCharacter : metaCharacters) {
            if (inputString.contains(metaCharacter)) {
                inputString = inputString.replace(metaCharacter, "\\" + metaCharacter);
            }
        }
        return inputString;
    }
}
