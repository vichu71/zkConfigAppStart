package cestel.sercom.web.repository.impl;


import java.util.List;
import java.util.Optional;

import cestel.sercom.web.entity.User;



/**
 * JPA repository for {@link LoginUserEntity}.
 */
public interface UserRepositoryImpl<T>{

	
	Optional<User> findByUsername(String login);

	List<User> getUserByNameContainingOrUsernameContainingOrInfoContaining(String name, String login,String description);

	List<User> findAll();
	
	Optional<User> findById(Long id);

	void deleteById(Long id);

	User save(User usuario);

	

}
