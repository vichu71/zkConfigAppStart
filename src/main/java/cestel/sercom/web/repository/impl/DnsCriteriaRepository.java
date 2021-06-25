package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.Dns;
import cestel.sercom.web.entity.User;


public interface DnsCriteriaRepository extends CrudRepository<Dns, String>,DnsRepositoryImpl<Dns>{

}
