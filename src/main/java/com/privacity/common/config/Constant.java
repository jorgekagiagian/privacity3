package com.privacity.common.config;

public class Constant {

	public final static String PROTOCOLO_COMPONENT_ENCRYPT_KEYS = "/encryptkeys";
	public final static String PROTOCOLO_ACTION_ENCRYPT_KEYS_GET = "/encryptkeys/get";
	public final static String PROTOCOLO_ACTION_ENCRYPT_KEYS_CREATE = "/encryptkeys/create";
	
	public final static String PROTOCOLO_COMPONENT_AUTH = "/auth";
	public final static String PROTOCOLO_ACTION_AUTH_LOGIN = "/auth/login";
	public final static String PROTOCOLO_ACTION_AUTH_REGISTER = "/auth/register";
	public final static String PROTOCOLO_ACTION_AUTH_VALIDATE_USERNAME = "/auth/validateUsername";
	
	public final static String PROTOCOLO_COMPONENT_MESSAGE = "/message"; 
	public final static String PROTOCOLO_ACTION_MESSAGE_RECIVIED = "/message/recivied";
	public final static String PROTOCOLO_ACTION_MESSAGE_CHANGE_STATE = "/message/changeState";
	public final static String PROTOCOLO_ACTION_MESSAGE_GET_ALL_ID_MESSAGE_UNREAD = "/message/getAllidMessageUnreadMessages";
	public static final String PROTOCOLO_ACTION_MESSAGE_GET_MESSAGE = "/message/get";
	
	
	public final static String PROTOCOLO_COMPONENT_GRUPOS = "/grupo";
	public static final String PROTOCOLO_ACTION_GRUPO_REMOVE_USER = "/grupo/remove/user";
	public static final String PROTOCOLO_ACTION_GRUPO_ADDUSER_ADDME = "/grupo/addUser/addMe";
	public static final String PROTOCOLO_ACTION_GRUPO_INVITATION_RECIVED = "/grupo/invitation/recived";
	
	public final static String PROTOCOLO_COMPONENT_GRUPOS_LIST_MY_GRUPOS = "/grupo/listar/misGrupos";
	

	public final static int MESSAGE_SEND_TIME_VALUE = 30000;

}
