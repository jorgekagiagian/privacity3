package com.privacity.server.main;


import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class SecretKeyPersonal {

	private String secretKeyAES;
	private String saltAES;

	private Cipher decrypt;
	private Cipher encrypt;

	public SecretKeyPersonal(String secretKeyAES, String saltAES) throws Exception {
		this.secretKeyAES = secretKeyAES;
		this.saltAES = saltAES;


		{
			byte[] iv = new byte[16];
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec keySpec = new PBEKeySpec((secretKeyAES).toCharArray(), (saltAES).getBytes(), 1, 128);
			SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
			SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
			decrypt = cipher;
			
		}
		{
			byte[] iv = new byte[16];
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec keySpec = new PBEKeySpec((secretKeyAES).toCharArray(), (saltAES).getBytes(), 1, 128);
			SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
			SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			encrypt=cipher;
		}
	}



	public String getAES(String data) {
		try {

			return Base64.getEncoder().encodeToString(encrypt.doFinal(data.getBytes(("UTF-8"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAESDecrypt(String data) throws Exception {
		
		
		if (data == null) return null;
		if (data == "") return "";

		String r = new String(decrypt.doFinal(Base64.getDecoder().decode(data)));
		//System.out.println("Entrada: " + data + " Salida: " + r);
		return r;


	}



}    

