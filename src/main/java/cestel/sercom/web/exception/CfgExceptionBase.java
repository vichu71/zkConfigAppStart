package cestel.sercom.web.exception;

/**
 * Esta es una clase "base" para todas las Excepciones de esta aplicacion.
 * <br>
 * NOTA: Todas las excepciones utilizadas en el EJB Module de CfgApp estan
 *       definidas dentro de este package "err" y toman a esta como base .
 * <br>
 * No contiene ningun tipo de información específico. Su unico objetivo es
 * reunir debajo de un ancestro "comun" a todas las excepciones especificas
 * de CfgApp, para poder distinguirlas de las excepciones estandar de Java,
 * y tratarlas en conjunto si fuera necesario.
 * @author dsusa
 */
public class CfgExceptionBase extends Exception
{

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3690198763000051553L;

 
  public CfgExceptionBase(Throwable t)
  {
    super(t);
  }

  /** Este CTOR permite "encadenar" excepciones, pero agregando un
   * mensaje detallado. 
   * (ver JDK Help, clase Throwable) */
  public CfgExceptionBase(String msg, Throwable t)
  {
    super(msg, t);
  }

  /** CTOR para definir un mensaje "custom".*/
  public CfgExceptionBase(String msg)
  {
    super(msg);
  }  
}
