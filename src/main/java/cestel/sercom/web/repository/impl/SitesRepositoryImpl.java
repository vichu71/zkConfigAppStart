package cestel.sercom.web.repository.impl;


import java.util.List;
import java.util.Optional;

import cestel.sercom.web.entity.Sites;

public interface SitesRepositoryImpl<T>{

	
	Optional<Sites> findById(Long id);

	Optional<Sites> findByName(String name);

	List<Sites> findAll();
	
	void deleteById(Long id);

	Sites save(Sites sites);

	List<Sites> getSitesByName(String filter);

}
