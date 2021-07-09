package cestel.sercom.web.util;

import java.io.FilenameFilter;

/**
 * XmlFileFilter.java
 * Created on 12 de agosto de 2005, 1:17
 * @author Administrador
 *
 * Filtro para obtener solo aquellos archivos cuya extension es ".XML"
 */
public class XmlFileFilter implements FilenameFilter {
    
// ==================== CTOR =========================================
    /** Creates a new instance of XmlFileFilter */
    public XmlFileFilter() {
    }
    
// ==================== CLASS METHODS ================================

    public boolean accept(java.io.File dir, String name) 
    {
        boolean retval = false;
        
        int dotPos = name.lastIndexOf('.');
        if( (dotPos>=0) && (dotPos<=name.length()-3) )
        {
            String fileExt = name.substring( dotPos+1);
            if(fileExt.compareToIgnoreCase("xml")==0)
                retval = true;
        }    
        return retval;
    }
    
}
