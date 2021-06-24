package cestel.sercom.web.repository.impl;


import java.util.List;

import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;



/**
 * JPA repository for {@link LoginUserEntity}.
 */
public interface ResourceRepositoryImpl<T>{

	//List<Roles> findAll();

	List<Resource> findByResclassAndSubdomid(String clase, SubDominio subdominio);
	
	List<Resource> getResourceByNameContainingOrInfoContaining(String name,String description);

	List<Resource> findAll();
	
	void deleteById(Long id);

	Resource save(Resource resource);

	List<Resource> findByResclass(String clase);
	
	

}
