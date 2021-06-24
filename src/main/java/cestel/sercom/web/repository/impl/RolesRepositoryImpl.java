package cestel.sercom.web.repository.impl;


import java.util.List;
import cestel.sercom.web.entity.Roles;



/**
 * JPA repository for {@link LoginUserEntity}.
 */
public interface RolesRepositoryImpl<T>{

	List<Roles> findAll();

}
