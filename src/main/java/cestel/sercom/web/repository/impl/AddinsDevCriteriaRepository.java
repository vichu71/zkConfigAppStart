package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.AddinsDev;


public interface AddinsDevCriteriaRepository extends CrudRepository<AddinsDev, String>,AddinsDevRepositoryImpl<AddinsDev>{

}
