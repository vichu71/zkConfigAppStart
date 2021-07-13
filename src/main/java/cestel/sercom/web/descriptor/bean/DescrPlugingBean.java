package cestel.sercom.web.descriptor.bean;

import lombok.Data;

@Data
public class DescrPlugingBean {
	public DescrPlugingBean(String desc, String plugins) {
		setDesc(desc);
		setPlugins(plugins);
		setDescplugins(desc+"Required plugins: "+plugins);
	}

	private String desc;

	private String plugins;
	
	private String descplugins;
}
