/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bapers.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author EdgarLaw
 */
// avaliable algorithm (MD2,MD5,SHA-1,SHA-224,SHA-256,SHA-384,SHA-512)
public class SimpleHash {

    /**
     *
     */
    public static final String HASH = "SHA-512";

    /**
     *
     * @param strings strings to hash
     * @return sha-512 hash of all inputs concatenated
     * @throws NoSuchAlgorithmException if the input hashing algorithm does
     * not exist
     */
    public static String getStringHash(String... strings)
            throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s);
        }
        return getStringHash(sb.toString().getBytes(), HASH);
    }

    /**
     *
     * @param stringBytes byte array representation of string to hash
     * @param algorithm to use to hash string
     * @return hash of stringBytes, using the supplied algorithm
     * @throws NoSuchAlgorithmException if the input hashing algorithm does
     */
    public static String getStringHash(byte[] stringBytes, String algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance(algorithm);
        m.update(stringBytes);
        byte[] bytesArray = m.digest();
        StringBuilder s = new StringBuilder(bytesArray.length * 2);
        for (byte b : bytesArray) {
            s.append((String.format("%02x", b)));
        }
        return s.toString();
    }

}
