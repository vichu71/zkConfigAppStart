package cestel.sercom.web.repository.impl;


import java.util.List;

import cestel.sercom.web.entity.PropResource;
import cestel.sercom.web.entity.Resource;


/**
 * JPA repository for {@link LoginUserEntity}.
 */
public interface PropResourceRepositoryImpl<T>{

		
	List<PropResource> findAll();
	
	void deleteById(Long id);

	PropResource save(PropResource resource);

	List<PropResource> findByResource(Resource resource);

	void deleteAllByResource(Resource resource);
	
	

}
