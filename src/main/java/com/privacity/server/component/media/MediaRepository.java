package com.privacity.server.component.media;

import org.springframework.data.repository.CrudRepository;

import com.privacity.server.model.Media;
import com.privacity.server.model.Message;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface MediaRepository extends CrudRepository<Media, Message> {

}
