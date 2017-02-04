package com.vmichalak.sonoscontroller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParserHelper {

    /**
     * Return the first find occurrence of a regex match.
     * @param regex pattern regex
     * @param content data
     * @return null if it doesn't found pattern
     */
    public static String findOne(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        boolean haveResult = matcher.find();
        if(!haveResult) { return null; }
        return matcher.group(1);
    }
}
