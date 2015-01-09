package com.china.acetech.ToolPackage.java;

import java.text.NumberFormat;

/**
 * Number tool of Java
 * Created by BXC2010011 on 2015/1/4.
 */
public class NumberTool {

    /**
     * @param value trans number
     * @param fraction number of fraction digit
     * @return format string of this number
     */
    public static String getStandardString(double value, int fraction){
        int integer = (int)value;
        if ( value - integer < 0.01d)
            return String.valueOf(integer);
        else{
            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(fraction);
            return format.format(value);
        }
    }
}
