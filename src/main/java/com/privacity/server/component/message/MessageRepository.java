package com.privacity.server.component.message;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.privacity.server.model.Grupo;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageId;
import com.privacity.server.security.Usuario;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface MessageRepository extends CrudRepository<Message, MessageId> {

	
//	@Transactional
//	@Modifying
//	@Query("insert into media (data, media_type, id_grupo, id_message) values (?1, ?2, ?3, ?4) ")
//	void insertMed(byte[] data, int media_type, Long id_grupo, Long id_message);
	
	List<Message> findByMessageIdGrupoIdGrupo(Long idGrupo);

	@Transactional
	@Modifying
	@Query("DELETE FROM Message e	WHERE e.messageId.grupo.idGrupo = ?1 and e.userCreation.idUser = ?2  ")
	void deleteAllMyMessageForEverybodyByGrupo(long parseLong, Long idUser);

	Message findByMessageIdIdMessage(long parseLong);

	@Transactional
	@Modifying
	@Query("DELETE FROM  Message u where "
			+ " u.userCreation = ?2  "
			+ " and u.messageId.grupo = ?1 ")
	void deleteAllMyMessagesByGrupo(Grupo grupo, Usuario usuarioLogged);

	@Query("SELECT u FROM Message u where "
			+ " u.userCreation = ?2  "
			+ " and u.anonimo = 1  "
			+ " and u.messageId.grupo = ?1 ")

	List<Message> findByMessageIdGrupoUserAnonimo(Grupo grupo, Usuario usuarioLogged);
}
