package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.DialRules;


public interface DialRulesCriteriaRepository extends CrudRepository<DialRules, String>,DialRulesRepositoryImpl<DialRules>{

}
