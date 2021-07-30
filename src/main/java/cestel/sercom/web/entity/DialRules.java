package cestel.sercom.web.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Data
@Entity
@Table(name = "rrules")
public class DialRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String      name;
    @Column
    private String      mediacode;
    @Column
    private String      accessprefix;
    @Column
    private String      accesssuffix;
   
    @OneToOne
    @JoinColumn(name = "domid", nullable = false)
    private Dominio      domid;
    @Column
    private String      enabled;
    @Column
    private String      accessregex;
    @Column
    private String      extraprefix;
    @Column
    private String      extrasuffix;
    @Column
    private String      intervalo;
    @Column
    private String      keepaccessprefix;
    @Column
    private String      keepaccesssuffix;
    @Column
    private String      keepcampaign;
    @Column
    private String      nodeid;
    
}