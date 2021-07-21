package cestel.sercom.web.repository.impl;


import java.util.List;

import cestel.sercom.web.entity.Addins;
import cestel.sercom.web.entity.AddinsProp;
import cestel.sercom.web.entity.PropResource;
import cestel.sercom.web.entity.Resource;


/**
 * JPA repository for {@link LoginUserEntity}.
 */
public interface PropAddinsRepositoryImpl<T>{

		
	List<AddinsProp> findAll();
	
	void deleteById(Long id);

	AddinsProp save(AddinsProp addinsProp);

	List<AddinsProp> findByAddins(Addins addins);

	void deleteAllByAddins(Addins addins);
	
	

}
