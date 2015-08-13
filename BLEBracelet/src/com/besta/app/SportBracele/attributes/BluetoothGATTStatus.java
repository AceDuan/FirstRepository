package com.besta.app.SportBracele.attributes;

import java.util.HashMap;

public class BluetoothGATTStatus
{
  private static HashMap<String, String> attributes = new HashMap<String, String>();

  static
  {
    attributes.put("257", "GATT_FAILURE - A GATT operation failed, errors other than the above.");
    attributes.put("5", "GATT_INSUFFICIENT_AUTHENTICATION - Insufficient authentication for a given operation.");
    attributes.put("15", "GATT_INSUFFICIENT_ENCRYPTION - Insufficient encryption for a given operation.");
    attributes.put("13", "GATT_INVALID_ATTRIBUTE_LENGTH - A write operation exceeds the maximum length of the attribute.");
    attributes.put("7", "GATT_INVALID_OFFSET - A read or write operation was requested with an invalid offset.");
    attributes.put("2", "GATT_READ_NOT_PERMITTED - GATT read operation is not permitted.");
    attributes.put("6", "GATT_REQUEST_NOT_SUPPORTED - The given request is not supported.");
    attributes.put("0", "GATT_SUCCESS - A GATT operation completed successfully.");
    attributes.put("3", "GATT_WRITE_NOT_PERMITTED - GATT write operation is not permitted");
    attributes.put("133", "GATT_ERROR");
    attributes.put("128", "GATT_NO_RESOURCES");
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
 * Qualified Name:     com.billy.service.attributes.BluetoothGATTStatus
 * JD-Core Version:    0.5.4
 */