package cestel.sercom.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cestel.sercom.web.entity.Roles;
import cestel.sercom.web.repository.impl.RolesRepositoryImpl;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Transactional
@Component("rolesMag")
public class RolesManager {

	@Autowired
	private RolesRepositoryImpl<?> rolesRepository;

	public List<Roles> getAllRoles() {
		return rolesRepository.findAll();
	}

}