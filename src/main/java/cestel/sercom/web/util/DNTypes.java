package cestel.sercom.web.util;

/**
 * Enumerado con los posibles "tipos" de DNs.
 */
public enum DNTypes
{
  UNKNOWN('?'), LOCAL('L'), REMOTE('R'), SERVICE('S');
  
  /** Codigo interno de tipo char. Se utiliza para persistencia, 
   * por lo cual NUNCA deberán cambiarse los valores ya definidos,
   * pues de hacerlo se invalidarian todos los datos guardados hasta
   * el momento. 
   * Este código debe permitir el "mapeo uno a uno" entre los valores
   * del enumerado y sus correspondientes representaciones mediante 
   * char. 
   */
  private final char dnTypeCode;   

  /** El CTOR permite inicializar cada Valor del Enumerado con un código
   * que se utilizará para persistencia.*/
  DNTypes(char aCode)
  {
    this.dnTypeCode = aCode; 
  }
 
  /** Metodo de ayuda para la visualizacion en Interface de Usuario*/
  public String toString()
  {
   String retval="";
   
    switch(this)
    {
      case UNKNOWN:    retval = "?";        break;
      case LOCAL:      retval = "Local";     break;
      case REMOTE:     retval = "Remoto";    break;
      case SERVICE:    retval = "Service";  break;
    }
    return retval;
  }

  /** Metodo de ayuda para la representacion en XML (ConfigMsg). */
  public String getTag()
  {
   String retval="";
   
    switch(this)
    {
      case LOCAL:      retval = "LOCAL";     break;
      case REMOTE:     retval = "REMOTE";    break;
      case SERVICE:    retval = "SERVICE";   break;
      case UNKNOWN:    
        throw new IllegalArgumentException("The type 'UNKNOWN' does not have tag for XML representation.");//dnTypes.java-dnt00
    }
    return retval;
  }
  
  /** Metodo de ayuda para obtener el Codigo (entero) de un determinado
   * valor del enumerado.*/
  public char toChar()
  {
    return dnTypeCode;
  }

  /** Metodo agregado para permitir "transformaciones" de los "enteros" 
   * (provenientes de la capa de persistencia) en valores del enumerado 
   * que puedan introducirse en el V.O. DN o cualquier otro. */
  public static DNTypes encode(char aCode)
  {
    switch(aCode)
    {
      case '?':     return DNTypes.UNKNOWN;
      case 'L':     return DNTypes.LOCAL;
      case 'R':     return DNTypes.REMOTE;
      case 'S':     return DNTypes.SERVICE;
    }  
    throw new IllegalArgumentException("Parameter 'aCode' invalid in DNTypes::encode(char).");//dnTypes.java-dnt01
  }  
}
