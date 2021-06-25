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
@Table(name = "addins_dev")
public class AddinsDev {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private String devgroup;

	@Column
	private String media;

}