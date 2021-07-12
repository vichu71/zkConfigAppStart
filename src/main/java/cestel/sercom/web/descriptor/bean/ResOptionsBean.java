package cestel.sercom.web.descriptor.bean;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Data

public class ResOptionsBean {

	private Long id;
	private String value;
	private String label;
	private Long idprop;

}