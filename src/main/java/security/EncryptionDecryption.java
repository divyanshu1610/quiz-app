/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;


import java.util.*;
import java.security.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.io.*;
/**
*
* @author Kushagra
*/
public class EncryptionDecryption {
    

private static final String ALGO = "AES";
private byte[] keyvalue;

/**
*
* @param key
*/
public EncryptionDecryption(String key){ //constructor
keyvalue = key.getBytes();
}
/**
*
* @param info
* @return
* @throws NoSuchAlgorithmException
* @throws NoSuchPaddingException
* @throws InvalidKeyException
* @throws IllegalBlockSizeException
* @throws BadPaddingException
*/

public String encryptData(String info)throws NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeyException,IllegalBlockSizeException, BadPaddingException{
//Encrption of Data
Key key = generateKey();
Cipher c = Cipher.getInstance(ALGO);
c.init(Cipher.ENCRYPT_MODE, key);
byte[] encryptVal = c.doFinal(info.getBytes());
String encryptedCode = Base64.getEncoder().encodeToString(encryptVal);
return encryptedCode;
}
/**
*
* @param encrypt
* @return
* @throws IOException
* @throws NoSuchAlgorithmException
* @throws NoSuchPaddingException
* @throws InvalidKeyException
* @throws IllegalBlockSizeException
* @throws BadPaddingException
*/
public String decryptData(String encrypt)throws IOException,NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeyException,IllegalBlockSizeException, BadPaddingException{
//Decryption of data
Key key = generateKey();
Cipher c = Cipher.getInstance(ALGO);
c.init(Cipher.DECRYPT_MODE, key);
byte[] decryptedCode = Base64.getMimeDecoder().decode(encrypt);
byte[] decryptVal = c.doFinal(decryptedCode);
String decryptedValue = new String(decryptVal);
return decryptedValue;
}
/**
*
* @return
*/
public Key generateKey(){
//Generate a secret key according to keyvalue and keyword
Key key = new SecretKeySpec(keyvalue, ALGO);
return key;
}
}