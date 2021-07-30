package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Data
@Entity
@Table(name = "rrule_cmp")
public class RruleCmp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "rruleid", nullable = false)
	private DialRules dialRules;
	
	@ManyToOne
	@JoinColumn(name = "resid", nullable = false)
	private Resource resource;

}