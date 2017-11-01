package com.vmichalak.sonoscontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserHelper {

    // Hide the implicit public constructor.
    private ParserHelper() { }

    /**
     * Return the first find occurrence of a regex match.
     * @param regex pattern regex
     * @param content data
     * @return an empty string if it doesn't found pattern
     */
    public static String findOne(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        boolean haveResult = matcher.find();
        if(!haveResult) { return ""; }
        return matcher.group(1);
    }

    /**
     * Return all occurrences of a regex match.
     * @param regex pattern regex
     * @param content data
     * @return an empty list if it doesn't found pattern
     */
    public static List<String> findAll(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        List<String> r = new ArrayList<String>();
        while (matcher.find()) {
            r.add(matcher.group(1));
        }
        return Collections.unmodifiableList(r);
    }
}
