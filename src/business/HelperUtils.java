package business;

import java.util.UUID;

/**
 * @author kojusujan1111@gmail.com 01/12/21
 */

public class HelperUtils {
    public static synchronized String generateUUID() {
        return UUID.randomUUID().toString();
    }


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
