package cestel.sercom.web.exception;

/**
 * Es una clase destinada a agrupar todas las Excepciones producidas al
 * fallar algun Comando de Configuracion en el Core.<br>
 * No contiene ningun tipo de información específico.
 * @author dsusa
 */
public class CoreCfgException extends CfgExceptionBase
{

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3690198763000051534L;

  /** Este CTOR permite "encadenar" excepciones. 
   * (ver Effective JAVA - Item 43) */
  public CoreCfgException(Throwable t)
  {
    super(t);
  }

  /** Este CTOR permite "encadenar" excepciones, pero agregando un
   * mensaje detallado. 
   * (ver JDK Help, clase Throwable) */
  public CoreCfgException(String msg, Throwable t)
  {
    super(msg, t);
  }

  /** CTOR para definir un mensaje "custom".*/
  public CoreCfgException(String msg)
  {
    super(msg);
  }  
}
