package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Data
@Entity
@Table(name = "s_domains")
public class Dominio {

    //region Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String      name;
    @Column
    private String      info;
    @Column
    private String      schema;
    @Column
    private String      utc;
    
    @OneToMany(mappedBy = "dominio")
	private List<SubDominio> subdominios;

  

}