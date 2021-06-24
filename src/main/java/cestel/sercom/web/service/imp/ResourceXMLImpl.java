package cestel.sercom.web.service.imp;

import java.util.Map;

import cestel.sercom.web.entity.Resource;

public interface ResourceXMLImpl {
	 /** Devuelve el "ConfigMsg" de creacion del objeto. 
	 * @param resource 
	 * @param modificacion */
    String createMsg(Resource resource, Boolean modificacion);
   /** Devuelve el "ConfigMsg" de eliminacion del objeto. 
 * @param resource */
    String deleteMsg(Resource resource);
    /** Devuelve el "ConfigMsg" de modificacion del objeto. 
     * @param modificacion 
     * @param resource 
     * @param prop */
    String updateMsg(Resource resource, Map<String, String> prop);

}
