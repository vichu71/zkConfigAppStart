package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Data
@Entity
@Table(name = "res_props")
public class PropResource {

	// region Properties
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne
    @JoinColumn(name = "resid")
    private Resource resource;
	
	@Column
	private String value;
	@Column
	private String name;

}