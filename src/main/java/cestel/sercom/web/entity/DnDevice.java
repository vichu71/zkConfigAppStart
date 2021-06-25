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
@Table(name = "dn_device")
public class DnDevice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "dnid", nullable = false)
	private Dns dns;
	
	@ManyToOne
	@JoinColumn(name = "devid", nullable = false)
	private AddinsDev addinsdev;

}