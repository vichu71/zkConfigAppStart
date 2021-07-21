package cestel.sercom.web.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Data
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
	


}