package com.privacity.server.component.message;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.privacity.common.enumeration.MessageState;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageDetail;
import com.privacity.server.model.MessageDetailId;
import com.privacity.server.security.Usuario;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface MessageDetailRepository extends CrudRepository<MessageDetail, MessageDetailId> {

	@Query("SELECT m FROM MessageDetail m WHERE m.messageDetailId.userDestino.idUser = ?1 and m.state not in (0,1, 4)")
	List<MessageDetail> getAllidMessageUnreadMessages(Long idUser);

	@Query("SELECT u FROM MessageDetail u WHERE u.messageDetailId.message.messageId.idMessage = ?1 and u.messageDetailId.userDestino.idUser = ?2")
	List<MessageDetail> findByMessageUser(Long idMessage, Long idUser);

	@Transactional
	@Modifying
	@Query("DELETE FROM MessageDetail u WHERE u.messageDetailId.message.messageId.grupo.idGrupo = ?1 and u.messageDetailId.userDestino.idUser = ?2")	
	void deleteAllMessageDetailByGrupoAndUser(Long idGrupo, Long idUser);

	@Transactional
	@Modifying
	@Query("DELETE FROM MessageDetail u WHERE u.messageDetailId.message in "
			+ " ( select e from Message e	WHERE e.messageId.grupo.idGrupo = ?1 and e.userCreation.idUser = ?2 ) ")
	void deleteAllMyMessageDetailForEverybodyByGrupo(Long idGrupo, Long idUser);

	@Transactional
	@Modifying
	@Query("UPDATE MessageDetail SET state = ?5 WHERE "
			+ " messageDetailId.message.messageId.grupo.idGrupo = ?2 "
			+ " and messageDetailId.message.messageId.idMessage = ?3 "
			+ " and messageDetailId.idMessageDetail = ?4 "
			+ " and messageDetailId.userDestino.idUser = ?1"
			+ "")
	void updateState(Long idUser , Long idGrupo, Long idMessage, Long idMessageDetail, MessageState state);


	@Query("Select u.messageDetailId.userDestino From MessageDetail u  WHERE "
			+ " u.messageDetailId.message.messageId.grupo.idGrupo = ?2 "
			+ " and u.messageDetailId.message.messageId.idMessage = ?3 "
			+ " and u.messageDetailId.userDestino.idUser != ?1"
			+ "")
	List<Usuario> findByUpdateState (Long idUser , Long idGrupo, Long idMessage);

}
