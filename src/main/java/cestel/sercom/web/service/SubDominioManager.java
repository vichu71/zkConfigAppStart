package cestel.sercom.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.repository.impl.SubDominioRepositoryImpl;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Transactional
@Component("subDomMag")
public class SubDominioManager {

	@Autowired
	private SubDominioRepositoryImpl<?> subDominioRepository;

	public List<SubDominio> getAllSubDominio() {
		return subDominioRepository.findAll();
	}
	
	public Optional<SubDominio> getLogin(Dominio dominio) {
        return  subDominioRepository.findByDominio(dominio);
    }

	public List<SubDominio> getSubDominioByDominio(Dominio dominio) {
		 return  subDominioRepository.getUserByDominio(dominio);
	}
	
	public List<SubDominio> getFiltered(String filter) {
		return subDominioRepository.getSubDominioByName(filter);
	}
	
	public void delete(SubDominio subDominio) {
		subDominioRepository.deleteById(subDominio.getId());
		
	}

	public void saveOrUpdate(SubDominio subDominio) {
		subDominioRepository.save(subDominio);
		
	}

}