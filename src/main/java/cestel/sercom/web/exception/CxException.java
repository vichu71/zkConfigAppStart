package cestel.sercom.web.exception;


/**
 * Es una clase de excepcion destinada a agrupar todas las Excepciones de
 * la Capa de Conectividad. En particular la conexion CORBA con el Core.<br>
 * No contiene ningun tipo de informaciÃ³n especÃ­fico.
 * @author dsusa
 */
public class CxException extends CoreCfgException
{

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3690198763000051522L;

  /** Este CTOR permite "encadenar" excepciones. 
   * (ver Effective JAVA - Item 43) */
  public CxException(Throwable t)
  {
    super(t);
  }

  /** Este CTOR permite "encadenar" excepciones, pero agregando un
   * mensaje detallado. 
   * (ver JDK Help, clase Throwable) */
  public CxException(String msg, Throwable t)
  {
    super(msg, t);
  }

  /** CTOR para definir un mensaje "custom".*/
  public CxException(String msg)
  {
    super(msg);
  }  
}
