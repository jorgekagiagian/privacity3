package com.privacity.server.encrypt;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.gson.Gson;
import com.privacity.common.dto.AESDTO;
import com.privacity.server.component.usuario.UsuarioService;
import com.privacity.server.exceptions.PrivacityException;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.main.SecretKeyPersonal;
import com.privacity.server.model.EncryptKeys;
import com.privacity.server.security.Usuario;
import com.privacity.server.security.UsuarioSessionIdInfo;

/**
 * Created by baiguantao on 2017/8/4.
 * User session record class
 */

public class CryptSessionRegistry{
    //this map save every session
    //This collection stores session
    private final ConcurrentMap<String, UsuarioSessionIdInfo> userSessionIds = new ConcurrentHashMap<String, UsuarioSessionIdInfo>();
    
   

    private UsuarioService usuarioService;
    
    private final Object lock = new Object();
    

    private static CryptSessionRegistry instance = new CryptSessionRegistry();
    
    public static CryptSessionRegistry getInstance() {
    	return instance;
    }
    private CryptSessionRegistry() {
 
    	    //for spring boot apps
    	    //ApplicationContext context =SpringApplication.run(ConseguridadApplication.class, new String[0]) 
    	usuarioService = (UsuarioService)StaticContextAccessor.getBean(UsuarioService.class);
  //  	 encryptKeysRepository = (EncryptKeysRepository)StaticContextAccessor.getBean(EncryptKeysRepository.class);
    }

    /**
     *
     * Get session Id
     * @param user
     * @return
     * @throws ValidationException 
     * @throws PrivacityException 
     */
    
    public UsuarioSessionIdInfo getSessionIds(String username) throws ValidationException {
    	try {
			return getSessionIds(usuarioService.getUsuarioForUsername(username));
		} catch (Exception e) {
			e.printStackTrace();
			return null; 
		}
    	
    }

    public UsuarioSessionIdInfo getSessionIds(Usuario user) throws Exception {
    	
    	if (this.userSessionIds.get(user.getUsername()) == null) {
    		String AES = generateRamdomSecretKeyAES();
    		String SaltAES = generateRamdomSaltAES();
    		
    		String AESEnc = "";
    		String SaltAESEnc = "";
    		RSA rsa = new RSA();
    		PublicKey publicKeyPrivacity=null;
    		EncryptKeys ek = user.getEncryptKeys();
    		
    		String privateKey = ek.getPrivateKey();
    		
    		{
    			
    		
    		    KeyFactory kf = KeyFactory.getInstance("RSA","SunRsaSign");
    		   
    		    
    		    X509EncodedKeySpec spec2 = new X509EncodedKeySpec( new Gson().fromJson(ek.getPublicKeyNoEncrypt(), byte[].class));	    
    		    publicKeyPrivacity = kf.generatePublic(spec2);	
    		    System.out.println(publicKeyPrivacity.toString());
    			
    		    {
    		    	byte[] enc = rsa.encryptFilePublic(AES.getBytes(), publicKeyPrivacity);
    		    	AESEnc = Base64.getEncoder().encodeToString(enc);
    		    }
    		    
    		    {
    		    	byte[] enc = rsa.encryptFilePublic(SaltAES.getBytes(), publicKeyPrivacity);
    		    	SaltAESEnc = Base64.getEncoder().encodeToString(enc);
    		    }

    		}
    		
    		AESDTO aes = new AESDTO();
    		{
    		
    		byte[] enc = rsa.encryptFilePublic("xxxxxxxxxxxxxxxxxxxxxxx".getBytes(), publicKeyPrivacity);
//			String encode = Base64.getEncoder().encodeToString(enc);
    		aes.setSecretKeyAES(AES);
    		}
    		UsuarioSessionIdInfo t = new UsuarioSessionIdInfo();
    		/*TODO cambiae a encriptado */
    		//t.setSessionAESDTOToSend(new AESDTO(AESEnc, SaltAESEnc));
    		t.setSessionAESDTOToSend(new AESDTO(AES, SaltAES));
    		t.setEncryptKeys(ek);
    		t.setRsa(rsa);
    		t.setPrivateKeyToSend(privateKey);
    		t.setPublicKey(publicKeyPrivacity);
    		t.setPublicKeyToSend(ek.getPublicKey());
    		
    		this.userSessionIds.put(user.getUsername(),t);
    		
    		try {
				t.setSecretKeyPersonal(new SecretKeyPersonal(AES, SaltAES));
			} catch (Exception e) {
				e.printStackTrace();
				throw new ValidationException("error de encruptcion");
			}
    	}
        UsuarioSessionIdInfo set = (UsuarioSessionIdInfo)this.userSessionIds.get(user.getUsername());
        
        return set;
        

    }
    
	private static String generateRamdomSecretKeyAES() {
		return RandomStringUtils.randomAlphanumeric(ConstantEncrypt.SESSION_AES_SECRET_KEY_LONG_GENERATOR_VALUE);
	}

	private static String generateRamdomSaltAES() {
		return RandomStringUtils.randomAlphanumeric(ConstantEncrypt.SESSION_AES_SALT_LONG_GENERATOR_VALUE);

	}

}