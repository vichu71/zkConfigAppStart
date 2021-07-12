package cestel.sercom.web.descriptor.bean;

import java.util.List;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Data
public class DeviceDesciptorBean {
	private Long id;
	private String family;
	private String version;
	private String type;
	private String descripcion;

}
