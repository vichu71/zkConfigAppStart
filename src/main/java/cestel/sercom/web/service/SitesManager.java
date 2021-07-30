package cestel.sercom.web.service;


import cestel.sercom.web.entity.Sites;
import cestel.sercom.web.repository.impl.SitesRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Transactional
@Component("siteMag")
public class SitesManager {

    @Autowired
    private SitesRepositoryImpl<?> siteRepository;

    public Optional<Sites> getDominioByName(String name) {
        return  siteRepository.findByName(name);
    }

	public List<Sites> getAllSites() {
		return  siteRepository.findAll();
	}
	public void delete(Sites sites) {
		siteRepository.deleteById(sites.getId());
		
	}

	public void saveOrUpdate(Sites sites) {
		siteRepository.save(sites);
		
	}

	
	public List<Sites> getFiltered(String filter) {
		return siteRepository.getSitesByName(filter);
	}



	
    

}