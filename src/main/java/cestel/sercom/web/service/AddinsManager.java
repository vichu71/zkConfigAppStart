package cestel.sercom.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cestel.sercom.web.entity.Addins;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.Roles;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.repository.impl.AddinsRepositoryImpl;
import cestel.sercom.web.repository.impl.ResourceRepositoryImpl;
import cestel.sercom.web.repository.impl.RolesRepositoryImpl;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Transactional
@Component("addinsMag")
public class AddinsManager {

	@Autowired
	private AddinsRepositoryImpl<?> addinsRepository;

	
	
	public List<Addins> getFiltered(String filter) {
		return addinsRepository.getAddinsByFamily(filter);
	}

	public List<Addins> getAll() {
		
		return (List<Addins>) addinsRepository.findAll();
		   
	}
	
	public void delete(Addins addins) {
		addinsRepository.deleteById(addins.getId());
		
	}

	public Addins saveOrUpdate(Addins addins) {
		return addinsRepository.save(addins);
		
	}

}