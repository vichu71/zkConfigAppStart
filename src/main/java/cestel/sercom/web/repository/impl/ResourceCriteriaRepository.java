package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.Resource;


public interface ResourceCriteriaRepository extends CrudRepository<Resource, String>,ResourceRepositoryImpl<Resource>{

}
