package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.Sites;


public interface SitesCriteriaRepository extends CrudRepository<Sites, String>,SitesRepositoryImpl<Sites>{

}
