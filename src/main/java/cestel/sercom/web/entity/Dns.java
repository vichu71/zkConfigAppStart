package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Data
@Entity
@Table(name = "dns")
public class Dns {

    //region Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String dntypecode;
    
    @Column
    private String mediacode;
    
    @Column
    private String remotepeer;
    
    @OneToOne
    @JoinColumn(name = "domid", nullable = false)
    private Dominio domid;
    
    @Column
    private String nodeid;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinTable(name="dn_device",
    joinColumns=@JoinColumn(name="dnid"),
    inverseJoinColumns=@JoinColumn(name="devid"))
    private AddinsDev addinsDev;
    
   
 

}