package com.privacity.server.component.media;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageId;

@Service
public class MediaUtilService {
	
	public Long generateIdMedia() {
		return Long.parseLong ((new Date().getTime()+"") + RandomStringUtils.randomNumeric(6));
	}	
}
