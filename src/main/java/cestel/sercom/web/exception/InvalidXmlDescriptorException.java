package cestel.sercom.web.exception;

/**
 * Es una clase de excepcion destinada a agrupar todas las Excepciones producidas
 * al parsear los Descriptores XML de Propiedades para Devices/Plugins.<br>
 * No contiene ningun tipo de información específico.
 * @author dsusa
 */
public class InvalidXmlDescriptorException extends CfgExceptionBase
{

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3690198763000051524L;

  /** Este CTOR permite "encadenar" excepciones. 
   * (ver Effective JAVA - Item 43) */
  public InvalidXmlDescriptorException(Throwable t)
  {
    super(t);
  }

  /** Este CTOR permite "encadenar" excepciones, pero agregando un
   * mensaje detallado. 
   * (ver JDK Help, clase Throwable) */
  public InvalidXmlDescriptorException(String msg, Throwable t)
  {
    super(msg, t);
  }

  /** CTOR para definir un mensaje "custom".*/
  public InvalidXmlDescriptorException(String msg)
  {
    super(msg);
  }  
}
