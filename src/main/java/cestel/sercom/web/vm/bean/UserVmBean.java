package cestel.sercom.web.vm.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserVmBean {
	
	@NotBlank
    private String username;

    @NotBlank
    private String password;

   
    private String dominio;
    
    private Long id;
	
	private String name;

	private String info;

	private String profilecode;

	private String domid;

	private String subdomid;
	
	private String team;


}
