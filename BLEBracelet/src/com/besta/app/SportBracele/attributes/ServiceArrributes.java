package com.besta.app.SportBracele.attributes;

import java.util.HashMap;

public class ServiceArrributes
{
  private static HashMap<String, String> attributes = new HashMap<String, String>();

  static
  {
    attributes.put("00001800-0000-1000-8000-00805f9b34fb", "Generic Access");
    attributes.put("00001801-0000-1000-8000-00805f9b34fb", "Generic Attribute");
    attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information");
    attributes.put("00001803-0000-1000-8000-00805f9b34fb", "Link Loss");
    attributes.put("00001802-0000-1000-8000-00805f9b34fb", "Immediate Alert");
    attributes.put("00001804-0000-1000-8000-00805f9b34fb", "Tx Power");
    attributes.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Service");
    attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate");
    attributes.put("0000180e-0000-1000-8000-00805f9b34fb", "Phone Alert Status Service");
    attributes.put("00001810-0000-1000-8000-00805f9b34fb", "Blood Pressure");
    attributes.put("00001811-0000-1000-8000-00805f9b34fb", "Alert Notification Service");
    attributes.put("00001805-0000-1000-8000-00805f9b34fb", "Current Time Service");
    attributes.put("00001818-0000-1000-8000-00805f9b34fb", "Cycling Power");
    attributes.put("00001816-0000-1000-8000-00805f9b34fb", "Cycling Speed and Cadence");
    attributes.put("00001808-0000-1000-8000-00805f9b34fb", "Glucose");
    attributes.put("00001809-0000-1000-8000-00805f9b34fb", "Health Thermometer");
    attributes.put("00001812-0000-1000-8000-00805f9b34fb", "Human Interface Device");
    attributes.put("00001819-0000-1000-8000-00805f9b34fb", "Location and Navigation");
    attributes.put("00001807-0000-1000-8000-00805f9b34fb", "Next DST Change Service");
    attributes.put("00001806-0000-1000-8000-00805f9b34fb", "Reference Time Update Service");
    attributes.put("00001814-0000-1000-8000-00805f9b34fb", "Running Speed and Cadence");
    attributes.put("00001813-0000-1000-8000-00805f9b34fb", "Scan Parameters");
    attributes.put("0000fff0-0000-1000-8000-00805f9b34fb", "0xFFF0");
    attributes.put("0000ffa0-0000-1000-8000-00805f9b34fb", "0xFFA0");
  }

  public static String lookup(String paramString1, String paramString2)
  {
    String str = (String)attributes.get(paramString1);
    if (str == null)
      return paramString2;
    return str;
  }
}

/* Location:           H:\backupforG\Projects\FA_Jack\apk\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.billy.service.attributes.ServiceArrributes
 * JD-Core Version:    0.5.4
 */