package cestel.sercom.web.repository.impl;


import java.util.List;

import cestel.sercom.web.entity.AddinsDev;


public interface AddinsDevRepositoryImpl<T>{

	List<AddinsDev> findAll();

	AddinsDev findByName(String name);

	AddinsDev findById(Long name);

}
