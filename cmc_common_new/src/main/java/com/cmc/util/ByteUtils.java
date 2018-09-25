package com.cmc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ByteUtils
{
  public static final Log logger = LogFactory.getLog(ByteUtils.class);
  public static final int BYTE_MODE_HH = 1;
  public static final int BYTE_MODE_LH = 2;
  private static char[] HEX_CODE = "0123456789ABCDEF".toCharArray();

  public static byte[] str2UTF8Bytes(String str)
  {
    return str2Bytes(str, "UTF-8");
  }

  public static byte[] str2GBKBytes(String str)
  {
    return str2Bytes(str, "GBK");
  }

  public static byte[] str2UTF16LE(String str)
  {
    return str2Bytes(str, "UTF16-LE");
  }

  public static byte[] str2UTF16BE(String str)
  {
    return str2Bytes(str, "UTF16-BE");
  }

  private static byte[] str2Bytes(String str, String charset) {
    try {
      return str.getBytes(charset);
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }

  public static byte[] int2Bytes(int number, int byteMode)
  {
    byte[] bytes = new byte[4];

    for (int i = 0; i < bytes.length; ++i) {
      int offset = (bytes.length - 1 - i) * 8;
      if (byteMode == 2)
        offset = i * 8;

      bytes[i] = (byte)(number >> offset & 0xFF);
    }

    return bytes;
  }

  public static byte[] short2Bytes(short number, int byteMode)
  {
    byte[] bytes = new byte[2];

    for (int i = 0; i < bytes.length; ++i) {
      int offset = (bytes.length - 1 - i) * 8;
      if (byteMode == 2)
        offset = i * 8;

      bytes[i] = (byte)(number >> offset & 0xFF);
    }

    return bytes;
  }

  public static int bytes2Int(byte[] data, int byteMode)
  {
    return bytes2Int(data, 0, data.length, byteMode);
  }

  public static int bytes2Int(byte[] data, int start, int length, int byteMode)
  {
    int value = 0;
    int vaildLength = (length > 4) ? 4 : length;

    for (int i = 0; i < vaildLength; ++i) {
      int shift = (vaildLength - 1 - i) * 8;
      if (byteMode == 2)
        shift = i * 8;

      value = value + ((data[(i + start)] & 0xFF) << shift);
    }
    return value;
  }

  public static short bytes2Short(byte[] data, int byteMode)
  {
    return bytes2Short(data, 0, data.length, byteMode);
  }

  public static short bytes2Short(byte[] data, int start, int length, int byteMode)
  {
    short value = 0;
    int vaildLength = (length > 2) ? 2 : length;

    for (int i = 0; i < vaildLength; ++i) {
      int shift = (vaildLength - 1 - i) * 8;
      if (byteMode == 2)
        shift = i * 8;

      value = (short)(value + ((data[(i + start)] & 0xFF) << shift));
    }
    return value;
  }

  public static byte[] reverse(byte[] data)
  {
    byte[] result = new byte[data.length];

    for (int i = 0; i < data.length; ++i) {
      result[i] = data[(data.length - 1 - i)];
    }

    return result;
  }

  public static String byte2HexString(byte b)
  {
    char[] cs = new char[2];
    cs[0] = HEX_CODE[(b >>> 4 & 0xF)];
    cs[1] = HEX_CODE[(b & 0xF)];
    return new String(cs);
  }

  public static String bytes2HexString(byte[] data)
  {
      if(data == null)
      {
          return "null";
      }
    return bytes2HexString(data, 0, data.length);
  }

  public static String bytes2HexString(byte[] data, int offset, int length)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < length; ++i)
      sb.append(byte2HexString(data[(i + offset)]));

    return sb.toString();
  }

  public static byte[] hexSring2bytes(String str)
  {
    if (str.length() % 2 == 1) {
      str = str + 'F';
    }

    byte[] ret = new byte[str.length() / 2];

    for (int i = 0; i < ret.length; ++i) {
      String bs = str.substring(2 * i, 2 * i + 2);
      ret[i] = (byte)Integer.parseInt(bs, 16);
    }

    return ret;
  }

  /**
   * 鑾峰彇鍓�8浣嶆寚瀹氫綅鐨勫紑鍏炽��
   * 
   * @param data
   * @param bitIndex
   * @return
   */
  public static  boolean getBitBoolean(int data, int bitIndex)
  {
      return ((data >> bitIndex) & 0x1) == 1;
  }

  /**
   * 灏嗘寚瀹氫綅璁緇
   * 
   * @param data
   * @param bitIndex
   * @return
   */
  public  static  int putBit(int data, int bitIndex)
  {
      return (data | (0x1 << bitIndex));
  }
  
  

  public static byte[] readBytesFromInStream(InputStream in)
  {
      try
      {
          ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
          byte[] buf = new byte[2048];
          int len = 0;
          try
          {
              while ((len = in.read(buf)) != -1) 
              {                
                  baos.write(buf, 0, len);
              }
          }
          catch (IOException e)
          {
              throw new RuntimeException(e);
          }
          return baos.toByteArray();
      }
      catch (Exception e)
      {
          throw new RuntimeException(e);
      }
      finally
      {
          if(in != null)
          {
              try 
              {
                  in.close();
              } catch (Exception e) 
              {
                  throw new RuntimeException(e);
              }
          }
      }
  }

  public static void writeByte2Stream(byte[] data,OutputStream os)
  {
      ByteArrayInputStream in = new ByteArrayInputStream(data);
      try 
      {
          byte[] buf = new byte[2048];
          int len = 0;
          while ((len = in.read(buf)) != -1) 
          {                
              os.write(buf, 0, len);
          }
          os.flush();
      } 
      catch (IOException e) 
      {
          throw new RuntimeException(e);
      } 
      finally 
      {
          if(os != null)
          {
        	  try 
              {
                  os.flush();
              } 
              catch (IOException e) 
              {
            	  logger.error("ByteUtils.writeByte2Stream.os.flush():"+e.getMessage(), e);
              }
              try 
              {
                  os.close();
              } 
              catch (IOException e) 
              {
            	  logger.error("ByteUtils.writeByte2Stream.os.close():"+e.getMessage(), e);
              }
          }
      }
  }
  
}