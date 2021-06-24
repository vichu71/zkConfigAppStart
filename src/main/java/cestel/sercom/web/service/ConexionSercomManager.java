package cestel.sercom.web.service;


import cestel.sercom.web.entity.User;
import cestel.sercom.web.exception.CxException;
import cestel.sercom.web.repository.impl.ConexionSercomImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author vmhuecas
 * @since may 2021
 */
@Component("conserMag")
public class ConexionSercomManager {

    @Autowired
    private ConexionSercomImpl conmexiSercImpl;
    
    public boolean isOnline() {
        return  conmexiSercImpl.isOnline();
    }
    public boolean isCoreConfigured() throws CxException {
        return  conmexiSercImpl.isCoreConfigured();
    }
    
    public String doConfig(String configMgs) throws CxException {
        return  conmexiSercImpl.doConfig(configMgs);
    }
    
//    conmexiSercImpl
    
   

}