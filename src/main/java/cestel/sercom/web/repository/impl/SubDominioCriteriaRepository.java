package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.SubDominio;


public interface SubDominioCriteriaRepository extends CrudRepository<SubDominio, String>,SubDominioRepositoryImpl<SubDominio>{

}
