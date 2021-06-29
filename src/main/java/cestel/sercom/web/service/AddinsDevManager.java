package cestel.sercom.web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cestel.sercom.web.entity.AddinsDev;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.repository.impl.AddinsDevRepositoryImpl;
import cestel.sercom.web.repository.impl.SubDominioRepositoryImpl;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Transactional
@Component("addinsDevMag")
public class AddinsDevManager {

	@Autowired
	private AddinsDevRepositoryImpl<?> addinsDevRepository;

	public List<AddinsDev> getAll() {
		return addinsDevRepository.findAll();
	}
	
	public AddinsDev getAddinsDevbyId(Long id) {
		return addinsDevRepository.findById(id);
	}

}