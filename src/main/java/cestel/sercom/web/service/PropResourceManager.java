package cestel.sercom.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cestel.sercom.web.entity.PropResource;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.Roles;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.repository.impl.PropResourceRepositoryImpl;
import cestel.sercom.web.repository.impl.ResourceRepositoryImpl;
import cestel.sercom.web.repository.impl.RolesRepositoryImpl;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Transactional
@Component("propresourceMag")
public class PropResourceManager {

	@Autowired
	private PropResourceRepositoryImpl<?> propresourceRepository;

	
	public List<PropResource> getAll() {
		
		return (List<PropResource>) propresourceRepository.findAll();
		   
	}
	
	public void delete(PropResource propresource) {
		propresourceRepository.deleteById(propresource.getId());
		
	}

	public PropResource saveOrUpdate(PropResource propresource) {
		return propresourceRepository.save(propresource);
		
	}
	
	public List<PropResource> findByResid(Resource resource) {
		return propresourceRepository.findByResource(resource);
	}

	public void deleteByResource(Resource resource) {
		propresourceRepository.deleteAllByResource(resource);
		
	}


}