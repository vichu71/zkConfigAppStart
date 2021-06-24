package cestel.sercom.web.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Data
@Entity
@Table(name = "s_subdomains")
public class SubDominio {

    //region Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String      name;
    @Column
    private String      info;
    @ManyToOne
    @JoinColumn(name = "dom_id")
    private Dominio      dominio;
    @Column
    private String      utc;

    

}