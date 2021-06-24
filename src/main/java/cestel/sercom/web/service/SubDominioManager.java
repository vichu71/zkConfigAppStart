package cestel.sercom.web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cestel.sercom.web.entity.SubDominio;
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

}