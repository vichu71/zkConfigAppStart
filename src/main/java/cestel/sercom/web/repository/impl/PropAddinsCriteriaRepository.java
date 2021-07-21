package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.AddinsProp;


public interface PropAddinsCriteriaRepository extends CrudRepository<AddinsProp, String>,PropAddinsRepositoryImpl<AddinsProp>{
	
}
