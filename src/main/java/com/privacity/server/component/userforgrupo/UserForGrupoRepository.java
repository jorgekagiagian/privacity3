package com.privacity.server.component.userforgrupo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.privacity.server.model.Grupo;
import com.privacity.server.model.UserForGrupo;
import com.privacity.server.model.UserForGrupoId;
import com.privacity.server.security.Usuario;




// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserForGrupoRepository extends CrudRepository<UserForGrupo, UserForGrupoId> {


	List<UserForGrupo> findByUserForGrupoIdUser(Usuario u);

	List<UserForGrupo> findByUserForGrupoIdGrupo(Grupo grupo);

	@Query("SELECT u FROM UserForGrupo u WHERE u.userForGrupoId.user.idUser = ?1")
	List<UserForGrupo> findByUserForGrupoIdUser(Long usuarioId);

	@Query("SELECT u.userForGrupoId.user FROM UserForGrupo u WHERE u.userForGrupoId.grupo.idGrupo = ?1")
	List<Usuario> findByUsersForGrupo(Long idGrupo);
}
