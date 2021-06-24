package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.PropResource;
import cestel.sercom.web.entity.Resource;


public interface PropResourceCriteriaRepository extends CrudRepository<PropResource, String>,PropResourceRepositoryImpl<PropResource>{
	
}
