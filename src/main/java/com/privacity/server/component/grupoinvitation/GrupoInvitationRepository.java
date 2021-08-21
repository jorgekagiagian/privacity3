package com.privacity.server.component.grupoinvitation;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.privacity.server.model.GrupoInvitation;
import com.privacity.server.model.GrupoInvitationId;
import com.privacity.server.security.Usuario;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface GrupoInvitationRepository extends CrudRepository<GrupoInvitation, GrupoInvitationId> {

	List<GrupoInvitation> findByGrupoInvitationIdUsuarioInvitado(Usuario u);


}
