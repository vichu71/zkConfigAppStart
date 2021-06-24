package cestel.sercom.web.util;

import java.io.FilenameFilter;

/**
 * Filtro para obtener solo aquellos archivos cuyo
 * nombre Comienza con una cierta Mascara y su extension es .XML
 * 
 * @author dsusa
 */
public class XmlFileMaskFilter implements FilenameFilter {
    
  private String mask="";
  
// ==================== CTOR =========================================
    /** Creates a new instance of XmlFileMaskFilter.
     * @param Mask, es la mascara que se buscara al principio del nombre
     * de cada archivo, para juzgar si es incluido en el Filtro. 
     * @throws IllegalArgumentException, si la mascara es nula o "". 
     * */
    public XmlFileMaskFilter(String Mask) {
      // Validacion de seguridad
      this.mask=Mask;
      if((Mask==null) || (Mask.length()==0))
        throw new IllegalArgumentException("An empty mask is not allowed in XmlFileMaskFilter.");//XmlFileMaskFilter.java-xfmf00
    }
    
// ==================== CLASS METHODS ================================

    public boolean accept(java.io.File dir, String name) 
    {
        boolean retval = false;
        
        int dotPos = name.lastIndexOf('.');
        if( (dotPos>=0) && (dotPos<=name.length()-3) )
        {
            String aux = name.substring( dotPos+1);
            if(aux.compareToIgnoreCase("xml")==0)
            {  
              // si hemos detectado la extension, buscamos la mascara
              aux = name.substring( 0, mask.length());
              if(aux.compareToIgnoreCase(mask)==0)
                retval = true;
            }    
        }    
        return retval;
    }
    
}
