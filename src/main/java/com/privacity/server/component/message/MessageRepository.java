package com.privacity.server.component.message;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.privacity.server.model.Grupo;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageId;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface MessageRepository extends CrudRepository<Message, MessageId> {

	List<Message> findByMessageIdGrupoIdGrupo(Long idGrupo);

	@Transactional
	@Modifying
	@Query("DELETE FROM Message e	WHERE e.messageId.grupo.idGrupo = ?1 and e.userCreation.idUser = ?2  ")
	void deleteAllMyMessageForEverybodyByGrupo(long parseLong, Long idUser);

	Message findByMessageIdIdMessage(long parseLong);

}
