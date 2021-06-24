package cestel.sercom.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.Roles;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.repository.impl.ResourceRepositoryImpl;
import cestel.sercom.web.repository.impl.RolesRepositoryImpl;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Transactional
@Component("resourceMag")
public class ResourceManager {

	@Autowired
	private ResourceRepositoryImpl<?> resourceRepository;

	public List<Resource> getResourcebyClassAndSubdominio(String clase,SubDominio subdominio) {
		return resourceRepository.findByResclassAndSubdomid(clase,subdominio);
	}
	
	public List<Resource> getResourcebyClase(String clase) {
		return resourceRepository.findByResclass(clase);
	}
	public List<Resource> getFiltered(String filter) {
		return resourceRepository.getResourceByNameContainingOrInfoContaining(filter, filter);
	}

	public List<Resource> getAll() {
		
		return (List<Resource>) resourceRepository.findAll();
		   
	}
	
	public void delete(Resource resource) {
		resourceRepository.deleteById(resource.getId());
		
	}

	public Resource saveOrUpdate(Resource resource) {
		return resourceRepository.save(resource);
		
	}

}