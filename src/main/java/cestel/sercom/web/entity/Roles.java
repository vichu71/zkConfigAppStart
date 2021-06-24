package cestel.sercom.web.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Data
@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String      nombre;
    @Column
    private String      descripcion;
    @Column
    private String      definicion;

  

}