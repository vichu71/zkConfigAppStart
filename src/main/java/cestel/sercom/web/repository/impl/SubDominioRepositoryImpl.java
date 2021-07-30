package cestel.sercom.web.repository.impl;


import java.util.List;
import java.util.Optional;

import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.entity.SubDominio;


public interface SubDominioRepositoryImpl<T>{

	List<SubDominio> findAll();

	Optional<SubDominio> findByDominio(Dominio dominio);

	List<SubDominio> getUserByDominio(Dominio dominio);

	List<SubDominio> getSubDominioByName(String filter);

	void deleteById(Long id);

	SubDominio save(SubDominio subDominio);

}
