package com.privacity.server.security;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.privacity.server.encrypt.ConstantEncrypt;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


	private String jwtSecret;
	
	@Value("${privacity.security.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	@Value("${privacity.security.ramdom.jwtSecret}")
	private boolean createRamdomJwtSecret;
	
	public String generateRamdomJwtSecret() {
		return RandomStringUtils.randomAscii(ConstantEncrypt.JWT_SECRET_LONG_GENERATOR_VALUE);
	}
	
	public JwtUtils(@Value("${privacity.security.ramdom.jwtSecret}")
	boolean createRamdomJwtSecret , @Value("${privacity.security.jwtSecret}") String jwtSecretDefault) {
		super();
		if (createRamdomJwtSecret) {
			jwtSecret= generateRamdomJwtSecret();
		}else {
			jwtSecret=jwtSecretDefault;
		}
	}

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
