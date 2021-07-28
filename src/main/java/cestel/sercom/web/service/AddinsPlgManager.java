package cestel.sercom.web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cestel.sercom.web.entity.AddinsDev;
import cestel.sercom.web.entity.AddinsPlg;
import cestel.sercom.web.entity.AddinsProp;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.repository.impl.AddinsDevRepositoryImpl;
import cestel.sercom.web.repository.impl.AddinsPlgRepositoryImpl;
import cestel.sercom.web.repository.impl.SubDominioRepositoryImpl;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Transactional
@Component("addinsPlgMag")
public class AddinsPlgManager {

	@Autowired
	private AddinsPlgRepositoryImpl<?> addinsPlgRepository;

	public List<AddinsPlg> getAll() {
		return addinsPlgRepository.findAll();
	}
	
	public AddinsPlg getAddinsPlgbyId(Long id) {
		return addinsPlgRepository.findById(id);
	}
	
	public AddinsPlg saveOrUpdate(AddinsPlg addinsPlg) {
		return addinsPlgRepository.save(addinsPlg);
		
	}

}