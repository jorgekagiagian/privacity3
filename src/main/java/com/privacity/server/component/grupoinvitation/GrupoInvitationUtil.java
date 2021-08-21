package com.privacity.server.component.grupoinvitation;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class GrupoInvitationUtil {

	public Long generateIdGrupoInvitation() {
		return Long.parseLong ((new Date().getTime()+"") + RandomStringUtils.randomNumeric(6));
	}	
}
