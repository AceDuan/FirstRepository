package com.china.acetech.ToolPackage.java;

import com.china.acetech.ToolPackage.MyApplication;

/**
 * 自定义的枚举类型，可以作为参考
 * Created by BXC2010011 on 2015/1/13.
 */
public class EnumSimple {
    public static enum WaterUnits{
        TEST(1, 1, 1, "", "");
        //OZ(R.string.unit_water_oz_short, R.string.unit_water_oz_short, R.string.unit_water_oz_short, "OZ", UserInfo_AP.VOLUME_OZ),
        //CUP(R.string.unit_water_cup_short, R.string.unit_water_cup_short, R.string.unit_water_cup_short, "CUPS", UserInfo_AP.VOLUME_CUP),
        //CUP(R.string.unit_water_oz_short, R.string.unit_water_oz_short, R.string.unit_water_oz_short, "CUPS", UserInfo_AP.VOLUME_CUP),
        //ML(R.string.unit_water_ml_short, R.string.unit_water_ml_short, R.string.unit_water_ml_short, "MILLILITER", UserInfo_AP.VOLUME_ML);
        private int pluralNameResId;
        private String serName;
        private int shortNameResId;
        private int userStringResId;
        private String unitFlag;

        private WaterUnits(int paramInt1, int paramInt2, int paramInt3, String paramString, String unitFlag)
        {
            this.userStringResId = paramInt1;
            this.shortNameResId = paramInt2;
            this.pluralNameResId = paramInt3;
            this.serName = paramString;
            this.unitFlag = unitFlag;
        }

        public String getDisplayName()
        {
            return MyApplication.getTopApp().getResources().getString(this.userStringResId);
        }

        public String getPluralName()
        {
            return MyApplication.getTopApp().getResources().getString(this.pluralNameResId);
        }

        public String getSerializableName()
        {
            return this.serName;
        }

        public String getShortDisplayName()
        {
            return MyApplication.getTopApp().getResources().getString(this.shortNameResId);
        }

        public String getVolumeUnitFlag(){
            return unitFlag;
        }
        public String toString()
        {
            return getDisplayName();
        }
    }
}
