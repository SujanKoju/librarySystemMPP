package business;

/**
 * @author kojusujan1111@gmail.com 01/12/21
 */

public class HelperUtils {
    public static boolean isBlankOrNull(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
