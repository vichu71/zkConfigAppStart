package cestel.sercom.web.service;


import cestel.sercom.web.entity.Dns;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.repository.impl.DnsRepositoryImpl;
import cestel.sercom.web.repository.impl.UserRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Transactional
@Component("dnsMag")
public class DnsManager {

    @Autowired
    private DnsRepositoryImpl<?> dnsRepository;

//    public Optional<User> getLogin(String username) {
//        return  userRepository.findByUsername(username);
//    }

	public List<Dns> getFiltered(String filter) {
		return dnsRepository.getDnsByNameContainingOrDntypecodeContainingOrMediacodeContaining(filter, filter, filter);
	}

	public List<Dns> getAll() {
		
		return (List<Dns>) dnsRepository.findAll();
		   
	}

	public Optional<Dns> isUsed(Long id) {
		return dnsRepository.findById(id);
	}

	public void delete(Dns dns) {
		dnsRepository.deleteById(dns.getId());
		
	}

	public void saveOrUpdate(Dns dns) {
		dnsRepository.save(dns);
		
	}

}