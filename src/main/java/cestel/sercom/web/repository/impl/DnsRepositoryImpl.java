package cestel.sercom.web.repository.impl;


import java.util.List;
import java.util.Optional;

import cestel.sercom.web.entity.Dns;

public interface DnsRepositoryImpl<T>{

	
	//Optional<User> findByUsername(String login);

	List<Dns> getDnsByNameContainingOrDntypecodeContainingOrMediacodeContaining(String name, String dntypecode,String mediacode);

	List<Dns> findAll();
	
	Optional<Dns> findById(Long id);

	void deleteById(Long id);

	Dns save(Dns dns);

	

}
