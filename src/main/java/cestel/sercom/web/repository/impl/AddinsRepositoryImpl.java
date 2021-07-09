package cestel.sercom.web.repository.impl;


import java.util.List;

import cestel.sercom.web.entity.Addins;


public interface AddinsRepositoryImpl<T>{

	
	//List<Addins> findByResclassAndSubdomid(String clase, SubDominio subdominio);
	
	//List<Addins> getResourceByNameContainingOrInfoContaining(String name,String description);

	List<Addins> findAll();
	
	void deleteById(Long id);

	Addins save(Addins addins);

	List<Addins> getAddinsByFamily(String filter);

	//List<Addins> findByResclass(String clase);
	
	

}
