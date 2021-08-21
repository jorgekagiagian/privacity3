package com.privacity.server.encrypt;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.Gson;


public class RSA {
 
	private KeyPair keyPair;
	private Key publicKey;
	private Key privateKey;
	
	/**
	  * Generar par de claves
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = new SecureRandom();
		pairgen.initialize(ConstantEncrypt.RSA_KEYSIZE, random);
		this.keyPair = pairgen.generateKeyPair();
		return this.keyPair;
	}
 
	/**
	  * Clave de cifrado
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 */
	public byte[] wrapKey(Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.WRAP_MODE, this.privateKey);
		byte[] wrappedKey = cipher.wrap(key);
		return wrappedKey;
	}
	
	/**
	  * Clave de descifrado
	 * @param wrapedKeyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	public Key unwrapKey(byte[] wrapedKeyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.UNWRAP_MODE, this.publicKey);
		Key key = cipher.unwrap(wrapedKeyBytes, "AES", Cipher.SECRET_KEY);
		return key;
	}
 
	public Key getPublicKey() {
		return publicKey;
	}
 
	public void setPublicKey(Key publicKey) {
		this.publicKey = publicKey;
	}
 
	public Key getPrivateKey() {
		return privateKey;
	}
 
	public void setPrivateKey(Key privateKey) {
		this.privateKey = privateKey;
	}
	
    public byte[] encryptFile(byte[] input, PrivateKey key) 
            throws IOException, GeneralSecurityException {
    	
    	   Cipher cipher;
    	    cipher = Cipher.getInstance("RSA");
    	        
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(input);
        }

    public byte[] encryptFilePublic(byte[] input, PublicKey key) 
            throws IOException, GeneralSecurityException {
    	
    	   Cipher cipher;
    	    cipher = Cipher.getInstance("RSA");
    	        
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(input);
        }
    
        public byte[] decryptFile(byte[] input, PublicKey key) 
            throws IOException, GeneralSecurityException {
     	   Cipher cipher;
   	    cipher = Cipher.getInstance("RSA");        	
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(input);
        }
        
        public byte[] decryptFilePrivate(byte[] input, PrivateKey key) 
                throws IOException, GeneralSecurityException {
         	   Cipher cipher;
       	    cipher = Cipher.getInstance("RSA");        	
                cipher.init(Cipher.DECRYPT_MODE, key);
                return cipher.doFinal(input);
            }        
	public static void main(String...strings ) throws Exception {
		RSA t = new RSA();
		KeyPair keyPair = t.generateKeyPair();
		
	    System.out.println(new Gson().toJson(keyPair.getPrivate().getEncoded()));
	    System.out.println(new Gson().toJson(keyPair.getPublic().getEncoded()));
	    
//		{
//		byte[] enc = t.encryptFile("xxxxxxxxxxxxxxxxxxxxxxxxxxxxssssssssssssssxxxxxxxxxxxxxxxxxxxxxx".getBytes(), ks.getPrivate());
//		String encode = Base64.getEncoder().encodeToString(enc);
//		// System.out.println(encode);
//		
//		 byte[] des = t.decryptFile(enc, ks.getPublic());
//		 //System.out.println(new String(des, StandardCharsets.UTF_8));
//		}
//		{
//			byte[] enc = t.encryptFilePublic("hola melina como estas".getBytes(), ks.getPublic());
//
//			String encode = Base64.getEncoder().encodeToString(enc);
//			// System.out.println(encode);
//			
//			 byte[] des = t.decryptFilePrivate(enc, ks.getPrivate());
//			 //System.out.println(new String(des, StandardCharsets.UTF_8));
//		}
	}
}