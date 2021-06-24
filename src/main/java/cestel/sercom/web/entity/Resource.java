package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since may 2021
 */
//@Data
@Entity
@Table(name = "resources")
public class Resource {

	// region Properties
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String name;
	
	@Column
	private String info;
	@Column
	private String nodeid;
	@Column
	private String resclass;

	@OneToOne
	@JoinColumn(name = "subdomid", nullable = false)
	private SubDominio subdomid;

	@OneToMany(mappedBy = "resource", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE }, orphanRemoval = true)
	private List<PropResource> propResource;
	
	@OneToMany(mappedBy = "resource", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, orphanRemoval = true)
	private List<UsrTeam> resourceUser;
	
	
	@Transient
	private String IdString;

	
	public Resource() {
		super();
		// TODO Auto-generated constructor stub
	}
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getResclass() {
		return resclass;
	}

	public void setResclass(String resclass) {
		this.resclass = resclass;
	}

	public SubDominio getSubdomid() {
		return subdomid;
	}

	public void setSubdomid(SubDominio subdomid) {
		this.subdomid = subdomid;
	}

	public List<PropResource> getPropResource() {
		return propResource;
	}

	public void setPropResource(List<PropResource> propResource) {
		this.propResource = propResource;
	}

	public String getIdString() {
		return Long.toString(id);
	}

	public void setIdString(String idString) {
		IdString = idString;
	}


}