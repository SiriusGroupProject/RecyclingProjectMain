package com.sirius.web.utils;

public class Utility {

    public static String trace(StackTraceElement e[]) {
        boolean doNext = false;
        for (StackTraceElement s : e) {
            if (doNext) {
                return s.getMethodName();
            }
            doNext = s.getMethodName().equals("getStackTrace");
        }
        return "";
    }

}
