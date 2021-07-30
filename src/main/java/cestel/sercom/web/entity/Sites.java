package cestel.sercom.web.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since jul 2021
 */
@Data
@Entity
@Table(name = "sites")
public class Sites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String      name;
    @Column
    private String      info;
    
}