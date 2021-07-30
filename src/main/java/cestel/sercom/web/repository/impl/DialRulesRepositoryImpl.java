package cestel.sercom.web.repository.impl;


import java.util.List;
import java.util.Optional;

import cestel.sercom.web.entity.DialRules;

public interface DialRulesRepositoryImpl<T>{

	
	Optional<DialRules> findById(Long id);

	Optional<DialRules> findByName(String name);

	List<DialRules> findAll();
	
	void deleteById(Long id);

	DialRules save(DialRules dialRules);

	List<DialRules> getDialRulesByName(String filter);

}
