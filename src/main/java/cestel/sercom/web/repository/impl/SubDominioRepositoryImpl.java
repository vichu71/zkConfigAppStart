package cestel.sercom.web.repository.impl;


import java.util.List;
import cestel.sercom.web.entity.SubDominio;



/**
 * JPA repository for {@link LoginUserEntity}.
 */
public interface SubDominioRepositoryImpl<T>{

	List<SubDominio> findAll();

}
