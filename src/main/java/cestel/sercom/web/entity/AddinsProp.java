package cestel.sercom.web.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vmhuecas
 * @since jul 2021
 */
//@Data
@Getter
@Setter
@Entity
@Table(name = "addins_prop")
public class AddinsProp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "addinsid")
    private Addins addins;
	
	@Column
	private String name;
	
	@Column
	private String value;

	public AddinsProp() {
		super();
		// TODO Auto-generated constructor stub
	}
	


}