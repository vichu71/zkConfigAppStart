package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Data
@Entity
@Table(name = "addins")
public class Addins {

    //region Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String family;

    @Column
    private String type;
    
    @Column
    private String aclass;
    
    @Column
    private String version;
    
   
    
    @Column
    private String nodeid;
    
//    @OneToOne(fetch=FetchType.EAGER)
//    @JoinTable(name="dn_device",
//    joinColumns=@JoinColumn(name="dnid"),
//    inverseJoinColumns=@JoinColumn(name="devid"))
//    private AddinsDev addinsDev;
    @OneToOne (fetch=FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    private AddinsDev addinsDev;
    
   
 

}