package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.User;


public interface UserCriteriaRepository extends CrudRepository<User, String>,UserRepositoryImpl<User>{

}
