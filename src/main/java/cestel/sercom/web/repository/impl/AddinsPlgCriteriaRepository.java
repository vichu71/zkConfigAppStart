package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.AddinsPlg;


public interface AddinsPlgCriteriaRepository extends CrudRepository<AddinsPlg, String>,AddinsPlgRepositoryImpl<AddinsPlg>{

}
