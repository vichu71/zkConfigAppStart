package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.Dominio;


public interface DominioCriteriaRepository extends CrudRepository<Dominio, String>,DominioRepositoryImpl<Dominio>{

}
