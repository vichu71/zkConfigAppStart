package cestel.sercom.web.repository.impl;


import java.util.List;

import cestel.sercom.web.entity.AddinsDev;
import cestel.sercom.web.entity.AddinsPlg;


public interface AddinsPlgRepositoryImpl<T>{

	List<AddinsPlg> findAll();

	AddinsPlg findById(Long name);

	AddinsPlg save(AddinsPlg addinsPlg);

}
