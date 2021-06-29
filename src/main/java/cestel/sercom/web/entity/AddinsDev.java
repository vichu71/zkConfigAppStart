package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jun 2021
 */
//@Data
@Entity
@Table(name = "addins_dev")
public class AddinsDev {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private String devgroup;

	@Column
	private String media;
	
	@Transient
	private String namedevroup;
	
	@Transient
	private String IdString;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDevgroup() {
		return devgroup;
	}

	public void setDevgroup(String devgroup) {
		this.devgroup = devgroup;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public String getNamedevroup() {
		return name+" ("+devgroup+")";
	}

	public String getIdString() {
		return Long.toString(id);
	}

	public void setIdString(String idString) {
		IdString = idString;
	}
	
	
	
	

}