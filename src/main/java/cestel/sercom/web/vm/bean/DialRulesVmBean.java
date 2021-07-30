package cestel.sercom.web.vm.bean;

import lombok.Data;

@Data
public class DialRulesVmBean {
	
	private Long id;
	
	private String preffix;

	private String suffix;
	
	private String regex;
	
	private String enabled;
	
		
}
