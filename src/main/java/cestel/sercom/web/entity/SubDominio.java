package cestel.sercom.web.entity;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author vmhuecas
 * @since may 2021
 */
//@Data
@Getter
@Setter
@NoArgsConstructor
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
    @LazyCollection(LazyCollectionOption.FALSE)
   // @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dom_id")
    private Dominio      dominio;
    @Column
    private String      utc;

    

}