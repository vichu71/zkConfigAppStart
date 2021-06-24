package cestel.sercom.web.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import cestel.sercom.web.repository.impl.UserCriteriaRepository;

@Repository
public abstract class UserRepository implements UserCriteriaRepository{
	  @PersistenceContext
	  EntityManager em;
	 

}
