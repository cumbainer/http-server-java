public class ParserHelper {

    public static String getUrlFromRequestPart(String part) {
        return part.split("\r\n")[0].split(" ")[1].substring(1);
    }


}
