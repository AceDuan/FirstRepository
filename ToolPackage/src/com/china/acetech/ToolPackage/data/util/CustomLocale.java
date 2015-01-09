package com.china.acetech.ToolPackage.data.util;

import java.util.Locale;

/**
 * Locale地區代碼設置和使用模塊
 *
 * @author bxc2010011
 */
public class CustomLocale {


    public static Locale getDefault() {
        Locale locale = Locale.getDefault();
        if (locale != null)
            return locale;
        return Locale.US;
    }

    public static String getLocaleString(Locale locale) {
        return locale.toString();
    }
}
