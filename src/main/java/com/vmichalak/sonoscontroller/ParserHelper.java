package com.vmichalak.sonoscontroller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParserHelper {
    public static String findOne(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        matcher.find();
        if(matcher.groupCount() < 1) { return null; }
        return matcher.group(1);
    }
}
