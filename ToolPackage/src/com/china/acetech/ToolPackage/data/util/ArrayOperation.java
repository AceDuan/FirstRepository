package com.china.acetech.ToolPackage.data.util;

import com.china.acetech.ToolPackage.data.domain.CodeInterFace;

public class ArrayOperation {
    public static double getValueofText(CharSequence text) {
        if (TempStringTool.isEmptyString(text))
            return 0.0D;
        return Double.parseDouble(text.toString());

    }

    public static long[] object2Value(Long[] array) {
        int i = 0;
        long[] resArray = new long[array.length];
        int j = array.length;
        for (int k = 0; i < j; k++) {
            Long value = array[i];
            resArray[k] = value;
            ++i;
        }
        return resArray;
    }

    public static Integer[] code2Object(CodeInterFace[] array) {
        int i = 0;
        Integer[] arrayOfInteger = new Integer[array.length];
        int j = array.length;
        for (int k = 0; i < j; k++) {
            CodeInterFace localj = array[i];
            arrayOfInteger[k] = localj.getCode();
            ++i;
        }
        return arrayOfInteger;
    }

    public static Long[] type2Object(long[] array) {
        int i = 0;
        Long[] resArray = new Long[array.length];
        int j = array.length;
        for (int k = 0; i < j; k++) {
            Long value = array[i];
            resArray[k] = value;
            ++i;
        }
        return resArray;
    }
}
