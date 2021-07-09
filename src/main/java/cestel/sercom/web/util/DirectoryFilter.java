package cestel.sercom.web.util;

import java.io.FileFilter;
/**
 * DirectoryFilter.java
 * Created on 12 de agosto de 2005, 0:50
 * @author Administrador
 *
 *
 */
public class DirectoryFilter implements FileFilter {
    
// ==================== CTOR =========================================
    /** Creates a new instance of DirectoryFilter */
    public DirectoryFilter() {
    }
    
// ==================== CLASS METHODS ================================

    public boolean accept(java.io.File pathname) {
        return pathname.isDirectory();
    }
    
}
