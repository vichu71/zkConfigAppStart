package cestel.sercom.web.vm.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import cestel.sercom.web.util.ComboDto;

@Getter
@Setter
public class DnsVmBean {
	
	private Long id;
	
	private String name;

	private String media;

	private String type;

	private String peer;
	
	private List<ComboDto> listMedia = new ArrayList<ComboDto>();
	
	private List<ComboDto> listType = new ArrayList<ComboDto>();
	
	private List<ComboDto> listDevice = new ArrayList<ComboDto>();
	
	private String nameDns;
	
	private ComboDto deviceComboSelecionado;
	
	private ComboDto mediaComboSelecionado;
	
	private ComboDto typeComboSelecionado;

     
	  

}
