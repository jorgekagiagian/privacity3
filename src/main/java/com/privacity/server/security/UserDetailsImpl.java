package com.privacity.server.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.privacity.common.dto.AESDTO;
import com.privacity.server.encrypt.CryptSessionRegistry;
import com.privacity.server.model.EncryptKeys;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long idUser;

	private String username;
	private String nickname;
	@JsonIgnore
	private String password;
	private AESDTO sessionEncrypt;
	private EncryptKeys encryptKeys;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long idUser, String username, String password,
			Collection<? extends GrantedAuthority> authorities, 
			String nickname, AESDTO sessionEncrypt, EncryptKeys encryptKeys) {
		this.idUser = idUser;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.nickname = nickname;
		this.sessionEncrypt = sessionEncrypt;
		this.encryptKeys = encryptKeys;
	}

	public static UserDetailsImpl build(Usuario user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		try {
		EncryptKeys ek =  new EncryptKeys();
		ek.setPrivateKey(CryptSessionRegistry.getInstance().getSessionIds(user.getUsername()).getPrivateKeyToSend());
		ek.setPublicKey(CryptSessionRegistry.getInstance().getSessionIds(user.getUsername()).getPublicKeyToSend());
		
	
			return new UserDetailsImpl(
					user.getIdUser(), 
					user.getUsername(), 
					user.getUsuarioPassword().getPassword(), 
					authorities,
					user.getNickname(),
					CryptSessionRegistry.getInstance().getSessionIds(user).getSessionAESDTOToSend(),
					ek
					
					);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return idUser;
	}


	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(idUser, user.idUser);
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public AESDTO getSessionEncrypt() {
		return sessionEncrypt;
	}

	public void setSessionEncrypt(AESDTO sessionEncrypt) {
		this.sessionEncrypt = sessionEncrypt;
	}

	public EncryptKeys getEncryptKeys() {
		return encryptKeys;
	}

	public void setEncryptKeys(EncryptKeys encryptKeys) {
		this.encryptKeys = encryptKeys;
	}
}
