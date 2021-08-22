package com.oracle.connect.sink;

/**
 * Created by jeremy on 5/3/16.
 */
class SqlVersionUtil {
  public static String getVersion() {
    try {
      return SqlVersionUtil.class.getPackage().getImplementationVersion();
    } catch(Exception ex){
      return "0.0.0.0";
    }
  }
}
