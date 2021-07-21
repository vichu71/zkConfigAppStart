package cestel.sercom.web.descriptor.bean;

import java.util.ArrayList;
import java.util.List;

import cestel.sercom.web.entity.AddinsDev;
import lombok.Data;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Data
public class DeviceDescriptorBean {
	private Long id;
	private String media;
	private String family;
	private String version;
	private String type;
	private DescrPlugingBean descripcionAndPlugind;
	private AddinsDev addinsDev;
	private List<ResClassPropBean> properties = new ArrayList<ResClassPropBean>();
}
