package cestel.sercom.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cestel.sercom.web.entity.Addins;
import cestel.sercom.web.entity.AddinsProp;
import cestel.sercom.web.entity.PropResource;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.Roles;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.repository.impl.PropAddinsRepositoryImpl;
import cestel.sercom.web.repository.impl.PropResourceRepositoryImpl;
import cestel.sercom.web.repository.impl.ResourceRepositoryImpl;
import cestel.sercom.web.repository.impl.RolesRepositoryImpl;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Transactional
@Component("propaddinsMag")
public class PropAddinsManager {

	@Autowired
	private PropAddinsRepositoryImpl<?> propAddinsRepository;

	
	public List<AddinsProp> getAll() {
		
		return (List<AddinsProp>) propAddinsRepository.findAll();
		   
	}
	
	public void delete(AddinsProp addinsProp) {
		propAddinsRepository.deleteById(addinsProp.getId());
		
	}

	public AddinsProp saveOrUpdate(AddinsProp addinsProp) {
		return propAddinsRepository.save(addinsProp);
		
	}
	
	public List<AddinsProp> findByResid(Addins addins) {
		return propAddinsRepository.findByAddins(addins);
	}

	public void deleteByAddins(Addins addins) {
		propAddinsRepository.deleteAllByAddins(addins);
		
	}


}