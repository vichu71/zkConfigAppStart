package cestel.sercom.web.descriptor.bean;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Data

public class ResClassPropBean {

	private Long id;
	private String name;
	private String clase;
	private String type;
	private Boolean advanced;
	private String descripcion;
	private Long optionrange;
	private String defval;
	private String resClassCode;

	private List<ResOptionsBean> resOptions;
	
}