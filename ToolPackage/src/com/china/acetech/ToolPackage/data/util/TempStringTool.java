package com.china.acetech.ToolPackage.data.util;

import java.util.Locale;

import android.content.Context;


public class TempStringTool {
    public static String getUpperString(Context context, int id) {
        return context.getResources().getString(id).toUpperCase(Locale.getDefault());
    }

    public static String getFirstUpperString(String string) {
        if (string.length() > 0)
            string = string.substring(0, 1).toUpperCase(Locale.getDefault())
                    + string.substring(1);
        return string;
    }

    public static String deleteCharAt(String string, int position) {
        if ((position < 0) || (position >= string.length()))
            return string;
        StringBuilder builder = new StringBuilder(-1
                + string.length());
        builder.append(string.substring(0, position)).append(
                string.substring(position + 1));
        return builder.toString();
    }

    public static boolean isEmptyString(CharSequence text) {
        return (text == null) || (text.length() == 0);
    }

    public static String getLowerString(Context context, int id) {
        return getFirstUpperString(context.getResources().getString(id).toLowerCase(Locale.getDefault()));
    }

    public static String getStringWithSingle(String string) {
        if (string == null)
            return null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            builder.append(c);
            builder.append("\n");
        }
        return builder.toString();
    }

    public static int getWidthCharLength(String string) {
        if (string != null)
            return 2 * string.length();
        return 0;
    }
}
