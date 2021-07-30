package cestel.sercom.web.service;


import cestel.sercom.web.entity.DialRules;
import cestel.sercom.web.repository.impl.DialRulesRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Transactional
@Component("dialRuleMag")
public class DialRulesManager {

    @Autowired
    private DialRulesRepositoryImpl<?> dialRuleRepository;

    public Optional<DialRules> getDominioByName(String name) {
        return  dialRuleRepository.findByName(name);
    }

	public List<DialRules> getAllDialRules() {
		return  dialRuleRepository.findAll();
	}
	public void delete(DialRules dialRules) {
		dialRuleRepository.deleteById(dialRules.getId());
		
	}

	public void saveOrUpdate(DialRules dialRules) {
		dialRuleRepository.save(dialRules);
		
	}

	
	public List<DialRules> getFiltered(String filter) {
		return dialRuleRepository.getDialRulesByName(filter);
	}



	
    

}