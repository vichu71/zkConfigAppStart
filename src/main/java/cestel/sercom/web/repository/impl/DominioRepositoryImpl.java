package cestel.sercom.web.repository.impl;


import java.util.List;
import java.util.Optional;

import cestel.sercom.web.entity.Dominio;



/**
 * JPA repository for {@link LoginUserEntity}.
 */
public interface DominioRepositoryImpl<T>{

	
	Optional<Dominio> findById(Long id);

	Optional<Dominio> findByName(String name);

	List<Dominio> findAll();

}
