package cestel.sercom.web.vm.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.zkoss.bind.annotation.DependsOn;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResourceVmBean {
	

	
	private String name;

	private String info;
	
	private String type;
	
	private String selecteditem;
	
	private String claseresource;
	
	private List<String> mensajes;
	
	


}
