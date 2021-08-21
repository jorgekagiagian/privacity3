package com.privacity.server.encrypt;

import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.privacity.common.annotations.PrivacityId;
import com.privacity.common.dto.GrupoDTO;
import com.privacity.common.dto.UserForGrupoDTO;
import com.privacity.common.dto.UsuarioDTO;

@Service
public class PrivacityIdServices {


	Cipher decrypt;
	Cipher encrypt;
	public PrivacityIdServices() throws Exception {
		{
			byte[] iv = new byte[16];
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec keySpec = new PBEKeySpec((ConstantEncrypt.PRIVACITY_ID_AES_SECRET_KEY).toCharArray(), (ConstantEncrypt.PRIVACITY_ID_AES_SALT).getBytes(), 1, 128);
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
			KeySpec keySpec = new PBEKeySpec((ConstantEncrypt.PRIVACITY_ID_AES_SECRET_KEY).toCharArray(), (ConstantEncrypt.PRIVACITY_ID_AES_SALT).getBytes(), 1, 128);
			SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
			SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			encrypt=cipher;
		}
	}



	public String getAES(String data) {
		try {

			return Base64.getEncoder().encodeToString(encrypt.doFinal(data.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAESDecrypt(String data) {
		
		
		if (data == null) return null;
		if (data == "") return "";
		System.out.println(data);

		try {

			return new String(decrypt.doFinal(Base64.getDecoder().decode(data)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public Object transformarEncriptarOut(Object g) throws IllegalAccessException, Exception, NoSuchFieldException, SecurityException {
		if (g == null) return null;

		//System.out.println(g.getClass().getName());


		if(
				(g.getClass().getName().equals("java.util.List"))
				|| 
				(g.getClass().getName().equals("java.util.ArrayList"))
				)
		{
			List list = (List)g;
			for ( int k = 0 ; k < list.size() ; k++) {
				transformarEncriptarOut(list.get(k));
			}

		} else if ( g.getClass().isArray()){
			Object[] lista = (Object[])g;
			for ( int k = 0 ; k < lista.length ; k++) {
				transformarEncriptarOut(lista[k]);
			}				
		}
		else  {
			for ( int i = 0 ; i < g.getClass().getDeclaredFields().length ; i++) {

				if ( g.getClass().getDeclaredFields()[i].toString().contains("[]")){
					Object[] lista = (Object[])g.getClass().getDeclaredFields()[i].get(g);
					if (lista == null) return null;
					for ( int k = 0 ; k < lista.length ; k++) {
						transformarEncriptarOut(lista[k]);
					}	
				} else if ((g.getClass().getDeclaredFields()[i].getType().getName().contains("com.privacity.common"))) {

					if (g.getClass().getDeclaredFields()[i].get(g) != null){
						transformarEncriptarOut(g.getClass().getDeclaredFields()[i].get(g));	
					}

				}else {

					if ( g.getClass().getDeclaredFields()[i].isAnnotationPresent(PrivacityId.class)) {
						//System.out.println(g.getClass().getDeclaredFields()[i]); 

						if (g.getClass().getDeclaredFields()[i].get(g) != null){
								try {
							g.getClass().getDeclaredFields()[i].set(g, this.getAES(g.getClass().getDeclaredFields()[i].get(g).toString()));
							//g.getClass().getDeclaredFields()[i].set(g,"xxxxxxxxx");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						}
					}



				} 



			}	
		}
		
		return g;
	}
	public Object transformarDesencriptarOut(Object g) throws IllegalAccessException, Exception, NoSuchFieldException, SecurityException {
		if (g == null) return null;

		//System.out.println(g.getClass().getName());


		if(
				(g.getClass().getName().equals("java.util.List"))
				|| 
				(g.getClass().getName().equals("java.util.ArrayList"))
				)
		{
			List list = (List)g;
			for ( int k = 0 ; k < list.size() ; k++) {
				transformarDesencriptarOut(list.get(k));
			}

		} else if ( g.getClass().isArray()){
			Object[] lista = (Object[])g;
			for ( int k = 0 ; k < lista.length ; k++) {
				transformarDesencriptarOut(lista[k]);
			}				
		}
		else  {
			for ( int i = 0 ; i < g.getClass().getDeclaredFields().length ; i++) {

				if ( g.getClass().getDeclaredFields()[i].toString().contains("[]")){
					Object[] lista = (Object[])g.getClass().getDeclaredFields()[i].get(g);
					if (lista == null) return null;
					for ( int k = 0 ; k < lista.length ; k++) {
						transformarDesencriptarOut(lista[k]);
					}	
				} else if ((g.getClass().getDeclaredFields()[i].getType().getName().contains("com.privacity.common"))) {

					if (g.getClass().getDeclaredFields()[i].get(g) != null){
						transformarDesencriptarOut(g.getClass().getDeclaredFields()[i].get(g));	
					}

				}else {

					if ( g.getClass().getDeclaredFields()[i].isAnnotationPresent(PrivacityId.class)) {
						//System.out.println(g.getClass().getDeclaredFields()[i]); 

						if (g.getClass().getDeclaredFields()[i].get(g) != null){

							try {
								g.getClass().getDeclaredFields()[i].set(g, this.getAESDecrypt(g.getClass().getDeclaredFields()[i].get(g).toString()));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
							//g.getClass().getDeclaredFields()[i].set(g,"xxxxxxxxx");

						}
					}



				} 



			}	
		}
		return g;
	}
	public static void main(String...strings ) throws Exception {
		PrivacityIdServices p = new PrivacityIdServices();
	/*
		GrupoDTO[] arr = new GrupoDTO[1];
		
		GrupoDTO o = new GrupoDTO();
		o.setIdGrupo("1");
		o.setName("name232");


		o.setUsersForGrupoDTO(new UserForGrupoDTO[1]);
		o.getUsersForGrupoDTO()[0]= new UserForGrupoDTO();
		o.getUsersForGrupoDTO()[0].setIdGrupo("323312");
		o.getUsersForGrupoDTO()[0].setRole("ROLE");
		o.getUsersForGrupoDTO()[0].setUsuario(new UsuarioDTO());
		o.getUsersForGrupoDTO()[0].getUsuario().setIdUsuario("21313");
		o.getUsersForGrupoDTO()[0].getUsuario().setNickname("feewfew");

		arr[0]=o;
		p.transformarEncriptarOut(arr);
		
		System.out.println(o.toString());
		p.transformarDesencriptarOut(arr);
		System.out.println(o.toString());
		*/
		System.out.println(p.getAES("hola"));
		
		System.out.println(p.getAESDecrypt("CjDTyilsYWIPtzZ7k6dE9w=="));
		
		
	}

}    

