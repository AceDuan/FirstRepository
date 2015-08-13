package com.besta.app.SportBracele.attributes;

public class DataFormatTransf
{
  public static byte[] hex2Byte(String paramString)
  {
    byte[] arrayOfByte = new byte[paramString.length() / 2];
    for (int i = 0; ; ++i)
    {
      if (i >= arrayOfByte.length)
        return arrayOfByte;
      arrayOfByte[i] = (byte)Integer.parseInt(paramString.substring(i * 2, 2 + i * 2), 16);
    }
  }

  public static byte[] hexStr2Bytes(String paramString)
  {
    int i = paramString.length() / 2;
    byte[] arrayOfByte = new byte[i];
    for (int j = 0; ; ++j)
    {
      if (j >= i)
        return arrayOfByte;
      int k = 1 + j * 2;
      int l = k + 1;
      arrayOfByte[j] = Byte.decode("0x" + paramString.substring(j * 2, k) + paramString.substring(k, l)).byteValue();
    }
  }

  public static byte[] hexStringToByteArray(String paramString)
  {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i / 2];
    for (int j = 0; ; j += 2)
    {
      if (j >= i)
        return arrayOfByte;
      arrayOfByte[(j / 2)] = (byte)((Character.digit(paramString.charAt(j), 16) << 4) + Character.digit(paramString.charAt(j + 1), 16));
    }
  }

  public static byte[] hexStringToByteArray2(String paramString)
  {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i / 2];
    for (int j = 0; ; j += 2)
    {
      if (j >= i)
        return arrayOfByte;
      arrayOfByte[(j / 2)] = (byte)((Character.digit(paramString.charAt(j), 16) << 4) + Character.digit(paramString.charAt(j + 1), 16));
    }
  }

  public static byte[] hexToBytes(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    int i = arrayOfChar.length / 2;
    byte[] arrayOfByte = new byte[i];
    for (int j = 0; ; ++j)
    {
      if (j >= i)
        return arrayOfByte;
      int k = Character.digit(arrayOfChar[(j * 2)], 16);
      int l = Character.digit(arrayOfChar[(1 + j * 2)], 16) | k << 4;
      if (l > 127);
      arrayOfByte[j] = (byte)(l -= 256);
    }
  }
}

/* Location:           H:\backupforG\Projects\FA_Jack\apk\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.billy.app.object.DataFormatTransf
 * JD-Core Version:    0.5.4
 */