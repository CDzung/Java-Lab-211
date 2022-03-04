/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mido
 */

public final class FormatString {

    public static String alignCenter(int spacing, Object obj) {

        String text = obj.toString();
        int spaces = spacing - text.length();
        if (spaces < 2) {
            return text;
        }
        return String.format("%" + (spaces / 2) + "s%s%" + (spaces - spaces / 2) + "s", "", text, "");
    }

    public static String toTitle(String str) {
        String[] s = str.trim().split("\\s+");
        str = "";
        for (String word : s) {
            str += Character.toUpperCase(word.charAt(0)) + word.toLowerCase().substring(1) + " ";
        }
        return str.trim();
    }

}
