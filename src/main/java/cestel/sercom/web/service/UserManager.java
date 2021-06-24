package cestel.sercom.web.service;


import cestel.sercom.web.entity.User;
import cestel.sercom.web.repository.impl.UserRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Transactional
@Component("userMag")
public class UserManager {

    @Autowired
    private UserRepositoryImpl<?> userRepository;

    public Optional<User> getLogin(String username) {
        return  userRepository.findByUsername(username);
    }

	public List<User> getFiltered(String filter) {
		return userRepository.getUserByNameContainingOrUsernameContainingOrInfoContaining(filter, filter, filter);
	}

	public List<User> getAll() {
		
		return (List<User>) userRepository.findAll();
		   
	}

	public Optional<User> isUsed(Long id) {
		return userRepository.findById(id);
	}

	public void delete(User user) {
		userRepository.deleteById(user.getId());
		
	}

	public void saveOrUpdate(User usuario) {
		userRepository.save(usuario);
		
	}

}