package com.evilsymphony.util;

public enum Color {
    GREEN("[32m", "[42m"),
    YELLOW("[33m", "[43m"),
    RED("[31m", "[41m");

    private final String fontCode;
    private final String backgroundCode;

    Color(String fontCode, String backgroundCode) {
        this.fontCode = fontCode;
        this.backgroundCode = backgroundCode;
    }

    /**
     * Creates a new string with the same text but with colored font.
     *
     * @param text The String to used to create the new colored one.
     * @return String with same text given, but displayed in colored font when printed.
     */
    public String setFontColor(String text) {

        return String.format("\033%s%s\033[0m", fontCode, text);
    }

    /**
     * Creates a new string with the same text but with colored background.
     *
     * @param text The String to used to create the new colored one.
     * @return String with same text given, but displayed in colored background when printed.
     */
    public String setBackgroundColor(String text) {

        return String.format("\033%s%s\033[0m", backgroundCode, text);
    }
}