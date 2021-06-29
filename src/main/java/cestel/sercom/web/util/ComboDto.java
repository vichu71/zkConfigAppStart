package cestel.sercom.web.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComboDto {
	private String value;
  	private String label;
  
  	public ComboDto(String value, String label) {
     	this.value =value;
      	this.label = label;
    }
  
  

}
