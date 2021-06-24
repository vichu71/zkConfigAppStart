package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.ResClassProp;


public interface DescriptorCriteriaRepository extends CrudRepository<ResClassProp, String>,DescriptorRepositoryImpl<ResClassProp>{

}
