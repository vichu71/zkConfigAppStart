package cestel.sercom.web.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Data
@Entity
@Table(name = "res_options")
public class ResOptions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String value;
	@Column
	private String label;
	@Column
	private Long idprop;
//	
//	
//		@ManyToOne(cascade = { CascadeType.MERGE })
//		@JoinColumn(name = "idprop")
//		
//		private ResClassProp resClassProp;

}