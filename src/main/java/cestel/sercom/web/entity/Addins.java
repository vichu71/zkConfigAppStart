package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    
    @OneToOne (fetch=FetchType.EAGER, cascade = { CascadeType.REMOVE }, orphanRemoval = true)
    @JoinColumn(name = "id", nullable = false)
    private AddinsDev addinsDev;
    
    @OneToOne (fetch=FetchType.EAGER, cascade = { CascadeType.REMOVE }, orphanRemoval = true)
    @JoinColumn(name = "id", nullable = false)
    private AddinsPlg addinsPlg;
    
    
    @OneToMany(mappedBy = "addins", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE }, orphanRemoval = true)
	 @LazyCollection(LazyCollectionOption.FALSE)
   @Fetch(value = FetchMode.SUBSELECT)
	
    private List<AddinsProp> addinsPropList;
    
    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(name="dn_device",
    joinColumns=@JoinColumn(name="devid"),
    inverseJoinColumns=@JoinColumn(name="dnid")
    )
    private List<Dns> dnsList;
    
   
 

}