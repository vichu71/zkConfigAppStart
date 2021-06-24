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
@Table(name = "users")
public class User {

    //region Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String info;
    
    @OneToOne
    @JoinColumn(name = "profilecode", nullable = false)
    private Roles profilecode;
    
    
    @OneToOne
    @JoinColumn(name = "domid", nullable = false)
    private Dominio domid;
    
    @OneToOne
    @JoinColumn(name = "subdomid", nullable = false)
    private SubDominio subdomid;
    @Column
    private String username;
    @Column
    private String password;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinTable(name="usr_team",
    joinColumns=@JoinColumn(name="userid"),
    inverseJoinColumns=@JoinColumn(name="resid"))
    private Resource resource;
    
//    @OneToOne(cascade = { CascadeType.REMOVE }, orphanRemoval = true)
//    @JoinColumn(name = "id")
//	private UsrTeam resourceUser;
    

  

}