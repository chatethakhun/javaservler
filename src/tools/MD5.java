/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author chate
 */
public class MD5 {
    public String MD5(String md5)  {
    	String password = md5;
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			 md.update(password.getBytes());

		        byte byteData[] = md.digest();

		        //convert the byte to hex format method 1
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < byteData.length; i++) {
		         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		        }

		        System.out.println("Digest(in hex format):: " + sb.toString());

		        //convert the byte to hex format method 2
		        StringBuffer hexString = new StringBuffer();
		    	for (int i=0;i<byteData.length;i++) {
		    		String hex=Integer.toHexString(0xff & byteData[i]);
		   	     	if(hex.length()==1) hexString.append('0');
		   	     	hexString.append(hex);
		    	}
		    	System.out.println("Digest(in hex format):: " + hexString.toString());
	
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return password;
    }
}
