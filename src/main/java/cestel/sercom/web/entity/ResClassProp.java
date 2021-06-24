package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Data
@Entity
@Table(name = "res_class_prop")
public class ResClassProp {

	// region Properties
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String name;
	@Column
	private String clase;
	@Column
	private String type;
	@Column
	private Boolean advanced;
	@Column
	private String descripcion;
	@Column
	private Long optionrange;
	@Column
	private String defval;
	@Transient
	private String resClassCode;

	@OneToMany(mappedBy = "idprop" , cascade = { CascadeType.ALL})
	private List<ResOptions> resOptions;
	
//	@OneToMany(mappedBy = "resClassProp" , cascade = { CascadeType.ALL})
//	private List<ResOptions> resOptions;
	
	

}