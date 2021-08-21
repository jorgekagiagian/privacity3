package com.privacity.common.enumeration;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ExceptionReturnCode {

	GRUPO_USER_NOT_EXISTS_INVITATION_CODE("G-1000", "No existe usuario con ese Invitation Code"),
	GRUPO_USER_IS_IN_THE_GRUPO("G-1001", "El usuario ya esta agregado en el grupo"),
	GRUPO_GRUPOID_BADFORMAT("G-1002", "ID GRUPO MAL FORMADO"),
	GRUPO_NOT_EXISTS("G-1003", "GRUPO NO EXISTE"),
	GRUPO_USER_IS_NOT_IN_THE_GRUPO("G-1004", "EL USUARIO NO PERTENECE AL GRUPO"),
	GRUPO_USER_NOT_HAVE_PERMITION_ON_THIS_GRUPO_TO_ADD_MEMBERS("G-1005", "EL USUARIO NO TIENE PERMISOS PARA AGREGAR A MIEMBROS"),
	GRUPO_ROLE_NOT_EXISTS("G-1006", "NO EXISTE EL ROL"),
	GRUPO_USER_INVITATION_CODE_IS_NULL("G-1007", "GRUPO_USER_INVITATION_CODE_IS_NULL"),
	GRUPO_INVITATION_EXIST_INVITATION("G-1008", "GRUPO_INVITATION_EXIST_INVITATION"),
	GRUPO_USER_INVITATION_CANT_BE_THE_SAME("G-1009", "GRUPO_USER_INVITATION_CANT_BE_THE_SAME"),
	
	ZIP_COMPRESS("Z-1001", "ERROR AL COMPRIMIR"),
	ZIP_DESCOMPRESS("Z-1002", "ERROR AL DESCOMPRIMIR"),
	
	USER_USER_SYSTEM_NOT_EXISTS("U-1000", "El usuario SYSTEM no existe"),
	USER_USER_NOT_LOGGER("U-1001", "El usuario no esta logueado"),
	USER_NICKNAME_IS_NULL("U-1002", "El nickname esta vacio"),
	USER_NICKNAME_TOO_LONG("U-1003", "El nickname es demasiado largo"),
	USER_NOT_EXISTS("U-1004", "USER_NOT_EXISTS"),
	
	
	AUTH_USERNAME_EXISTS("A-1000", "El nombre de usuario existe"),
	AUTH_USERNAME_IS_NULL("A-1001", "El nombre de usuario es nulo"),
	AUTH_USERNAME_IS_TOO_SHORT("A-1002", "El nombre de usuario es demasiado corto"),
	
	MESSAGE_MESSAGEID_BADFORMAT("M-1000", "ID MESSAGE MAL FORMADO"),
	MESSAGE_NOT_EXISTS("M-1001", "MESSAGE NO EXISTE"),
	MESSAGE_NOT_MESSAGE_CREATOR("M-1002", "MESSAGE SOS EL CREADOR DEL MENSAJE"),
	
	MESSAGEDETAIL_MESSAGEDETAILID_BADFORMAT("MD-1000", "ID MESSAGEDETAIL MAL FORMADO"),
	MESSAGEDETAIL_NOT_EXISTS("MD-1001", "MESSAGEDETAIL NO EXISTE"),
	MESSAGEDETAIL_IS_DELETED("MD-1002", "MESSAGEDETAIL IS DELETED"),
	MESSAGEDETAIL_NOT_EXISTS_TIME_MESSAGE("MD-1003", "MESSAGEDETAIL_NOT_EXISTS_TIME_MESSAGE"),
	
	MYACCOUNT_INVITATION_CODE_NOT_AVAIBLE("MY-1000", "MYACCOUNT_INVITATION_CODE_NOT_AVAIBLE"),
	MYACCOUNT_INVITATION_CODE_CANT_BE_EMPTY("MY-1001", "MYACCOUNT_INVITATION_CODE_CANT_BE_EMPTY"),
	
	ENCRYPT_IS_NULL("E-1000", "ENCRYPT_IS_NULL"),
	ENCRYPT_PRIVATE_KEY_IS_NULL("E-1001", "ENCRYPT_PRIVATE_KEY_IS_NULL"),
	ENCRYPT_PUBLIC_KEY_IS_NULL("E-1002", "ENCRYPT_PUBLIC_KEY_IS_NULL"),
	ENCRYPT_PUBLIC_KEY_NO_ENCRIPT_IS_NULL("E-1003", "ENCRYPT_PUBLIC_KEY_NO_ENCRIPT_IS_NULL"),
	ENCRYPT_PUBLIC_KEY_MUST_BE_NULL("E-1006", "ENCRYPT_PUBLIC_KEY_MUST_BE_NULL"),
	
	ENCRYPT_AES_IS_NULL("E-1003", "ENCRYPT_AES_IS_NULL"),
	ENCRYPT_AES_SECRET_KEY_IS_NULL("E-1004", "ENCRYPT_AES_SECRET_KEY_IS_NULL"),
	ENCRYPT_AES_SALT_IS_NULL("E-1005", "ENCRYPT_AES_SALT_IS_NULL"),
	ENCRYPT_PROCESS("E-1006", "ENCRYPT_PROCESS");
	
	private static final String CONSTANT_DESCRIPTION = "description";
	private static final String CONSTANT_CODE = "code";

	private final String code;
	private final String description;

  private ExceptionReturnCode(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public String getDescription() {
     return description;
  }

  public String getCode() {
     return code;
  }  
  
  public String toShow() {
	     return getCode() + " - " + getDescription();
  }

  public String toShow(String exceptionMessage) {
		return exceptionMessage + " - " + toString();
	}  
  
  public Map<String, String> toReturn() {
	  Map<String, String> r = new LinkedHashMap<String, String>();
	  r.put(CONSTANT_CODE, code );
	  r.put(CONSTANT_DESCRIPTION, description );
	  return r;
  }  
  
  public static ExceptionReturnCode getByCode(String code) {
	  ExceptionReturnCode[] e = ExceptionReturnCode.class.getEnumConstants();
	  
	  for (int i=0 ; i < e.length ; i++ ) {
		  if (e[i].getCode().equals(code)) {
			  return e[i];
		  }
	  }
	  return null;
  }
}