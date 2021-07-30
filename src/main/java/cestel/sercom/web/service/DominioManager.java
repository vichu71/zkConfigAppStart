package cestel.sercom.web.service;


import cestel.sercom.web.entity.Dns;
import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.repository.impl.DominioRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Transactional
@Component("domMag")
public class DominioManager {

    @Autowired
    private DominioRepositoryImpl<?> dominioRepository;

    public Optional<Dominio> getDominioByName(String name) {
        return  dominioRepository.findByName(name);
    }

	public List<Dominio> getAllDominio() {
		return  dominioRepository.findAll();
	}
	public void delete(Dominio dominio) {
		dominioRepository.deleteById(dominio.getId());
		
	}

	public void saveOrUpdate(Dominio dominio) {
		dominioRepository.save(dominio);
		
	}

	
	public List<Dominio> getFiltered(String filter) {
		return dominioRepository.getDominioByName(filter);
	}



	
    

}