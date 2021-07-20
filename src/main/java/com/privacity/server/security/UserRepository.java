package com.privacity.server.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByUsername(String username);

	Boolean existsByUsername(String username);

}
