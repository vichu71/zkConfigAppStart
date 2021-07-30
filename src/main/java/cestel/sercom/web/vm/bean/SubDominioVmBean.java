package cestel.sercom.web.vm.bean;

import java.util.ArrayList;
import java.util.List;

import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.util.ComboDto;
import lombok.Data;

@Data
public class SubDominioVmBean {
	
	private Long id;
	
	private String name;

	private String info;
	
	private Dominio dominio;
	
	private String utc;
	
}
