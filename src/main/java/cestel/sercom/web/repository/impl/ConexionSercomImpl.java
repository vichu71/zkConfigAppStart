package cestel.sercom.web.repository.impl;

import cestel.sercom.web.exception.CxException;

/**
 * JPA repository for {@link LoginUserEntity}.
 */
public interface ConexionSercomImpl{

	
	boolean isOnline();
	
	boolean isCoreConfigured() throws CxException;

	String doConfig(String XmlCfg) throws CxException;

}
