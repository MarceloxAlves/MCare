package br.com.marcelo.mcare.utils;

/**
 * Created by Marcelo on 06/03/2018.
 */

public class TextoFormato {
    public static String LPad(String word, int length, char ch) {
        return (length > word.length()) ? LPad(ch + word, length, ch) : word;
    }
}
