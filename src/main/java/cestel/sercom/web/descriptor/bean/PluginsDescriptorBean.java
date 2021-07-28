package cestel.sercom.web.descriptor.bean;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Data
public class PluginsDescriptorBean {
	private Long id;
	
	private String family;
	private String version;
	private String type;
	private String descripcion;
	
}
