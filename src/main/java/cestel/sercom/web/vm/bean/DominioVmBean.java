package cestel.sercom.web.vm.bean;

import java.util.ArrayList;
import java.util.List;

import cestel.sercom.web.util.ComboDto;
import lombok.Data;

@Data
public class DominioVmBean {
	
	private Long id;
	
	private String name;

	private String info;
	
	private String schema;
	
	private String utc;
	
	private List<ComboDto> listDominios = new ArrayList<ComboDto>();
	private ComboDto dominioComboSelecionado;
	
}
