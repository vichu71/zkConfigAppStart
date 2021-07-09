package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.Addins;


public interface AddinsCriteriaRepository extends CrudRepository<Addins, String>,AddinsRepositoryImpl<Addins>{

}
