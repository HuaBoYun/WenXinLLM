package com.hbfk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class SerializeUtil {
      public static byte[] serialize(Object object) {
           ObjectOutputStream oos = null;
            ByteArrayOutputStream baos = null;
            try {
                 // 序列化
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                 byte[] bytes = baos.toByteArray();
                 return bytes;
           } catch (Exception e) {

           }
            return null;
     }

      public static Object unserialize( byte[] bytes) {
           ByteArrayInputStream bais = null;
            try {
                 // 反序列化
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                 return ois.readObject();
           } catch (Exception e) {

           }
            return null;
     }
      
      public static String [][] getArrayForMap(Map map){
    	 // Map<String, Object> map = new HashMap<String, Object>();  
    	
    	    
    	  String[][] twoDarray = new String[map.size()][2];  
    	    
    	  String[] keys =(String[]) map.keySet().toArray();  
    	  Object[] values =map.values().toArray();  
    	    
    	  for (int row = 0; row < twoDarray.length; row++) {  
    	      twoDarray[row][0] = keys[row];  
    	      twoDarray[row][1] = values[row].toString();  
    	  }  
    	  return twoDarray;
      }
}
