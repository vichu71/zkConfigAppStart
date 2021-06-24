package cestel.sercom.web.repository.impl;

import org.springframework.data.repository.CrudRepository;

import cestel.sercom.web.entity.Roles;


public interface RolesCriteriaRepository extends CrudRepository<Roles, String>,RolesRepositoryImpl<Roles>{

}
