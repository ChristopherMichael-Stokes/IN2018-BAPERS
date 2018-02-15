/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author EdgarLaw
 */
// avaliable algorithm (MD2,MD5,SHA-1,SHA-224,SHA-256,SHA-384,SHA-512)
public class SimpleHash {

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        String test = "Try whatever you want here!";
//        // String on second parameter can be change by algorithm listed on top
//        System.out.println(getStringHash(test.getBytes(),"SHA-512"));
//        System.out.println(getStringHash(test.getBytes(),"MD2"));
//    }
    public static String getStringHash(byte[] stringBytes, String algorithm) {
        try {
            MessageDigest m = MessageDigest.getInstance(algorithm);
            m.update(stringBytes);
            byte[] bytesArray = m.digest();
            StringBuilder s = new StringBuilder(bytesArray.length * 2);
            for (byte b : bytesArray)
                s.append((String.format("%02x", b)));
            return s.toString();
//            hashValue = DatatypeConverter.printHexBinary(bytesArray).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            return "error";
        }
    }

}
