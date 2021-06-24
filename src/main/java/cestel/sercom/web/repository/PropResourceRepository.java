package cestel.sercom.web.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import cestel.sercom.web.entity.PropResource;
import cestel.sercom.web.entity.PropResource_;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.repository.impl.PropResourceCriteriaRepository;

@Repository
public abstract class PropResourceRepository implements  PropResourceCriteriaRepository{
	  @PersistenceContext
	  EntityManager em;
	  
//	  @Override
//	  public void deleteAllByResource(Resource resource) {
//		  
//		  CriteriaBuilder cb =  em.getCriteriaBuilder();
//			CriteriaDelete<PropResource> delete = cb.createCriteriaDelete(PropResource.class);
//			Root<PropResource> root = delete.from(PropResource.class);
//			List<Predicate> predicateList = new ArrayList<Predicate>();
//			Predicate[] predicateArray = new Predicate[predicateList.size()];
//			
//			
//			delete.where(cb.equal(root.get(PropResource_.resource), resource));
//			 
//	        // perform update
//	        this.em.createQuery(delete).executeUpdate();
//	  }

}
