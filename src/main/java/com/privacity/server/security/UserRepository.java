package com.privacity.server.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.privacity.server.model.UsuarioInvitationCode;



@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByUsername(String username);

	Boolean existsByUsername(String username);

	@Query("SELECT u FROM Usuario u where "
			+ " u.idUser = (select a.usuario.idUser from UsuarioInvitationCode a where   "
			+ " a.invitationCode = ?1 )  " )
	Usuario findByUsuarioInvitationCode(String invitationCode);

	//Usuario findByUsuarioInvitationCode(UsuarioInvitationCode invitationCodeToAdd);
	
	

}
