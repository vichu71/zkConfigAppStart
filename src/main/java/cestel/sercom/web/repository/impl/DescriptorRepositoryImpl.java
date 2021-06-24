package cestel.sercom.web.repository.impl;


import cestel.sercom.web.entity.ResClassProp;



/**
 * JPA repository for {@link LoginUserEntity}.
 */
public interface DescriptorRepositoryImpl<T>{

	
//	Optional<ResClassProp> findById(Long id);
	
	ResClassProp save(ResClassProp resClassProp);
	
	//void delete();

	void deleteAll();

	
}
