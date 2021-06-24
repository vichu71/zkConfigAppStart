package cestel.sercom.web.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "usr_team")
public class UsrTeam {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "userid", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "resid", nullable = false)
	private Resource resource;
	
//	@ManyToOne
//    @JoinColumn(name = "resid")
//    private Resource resource;

}
