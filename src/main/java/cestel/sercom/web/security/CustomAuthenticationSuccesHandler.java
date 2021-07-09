package cestel.sercom.web.security;

import java.io.IOException;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.service.DominioManager;
import cestel.sercom.web.service.UserManager;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class CustomAuthenticationSuccesHandler implements AuthenticationSuccessHandler {

	
	@Autowired
    private UserManager userMag;
	
	@Autowired
    private DominioManager domMag;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String userName = "";
        HttpSession session = request.getSession();
        Collection< GrantedAuthority > authorities = null;
        if(authentication.getPrincipal() instanceof Principal ) {
            userName = ((Principal)authentication.getPrincipal()).getName();
            session.setAttribute("role", "none");
        }else {
            User userSpringSecu = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            session.setAttribute("role", String.valueOf( userSpringSecu.getAuthorities()));
            cestel.sercom.web.entity.User usuarioDB =  userMag.getLogin( userSpringSecu.getUsername() ).orElseThrow(() -> new UsernameNotFoundException("Username does not exist")) ;
            session.setAttribute( "connectedUser" ,usuarioDB);
           //controlamos el dominio
           
           
           //si el usuario es distinto al sysadmin necesita dominio y que coincida
           if(!usuarioDB.getProfilecode().getId().equals(400L) ) {
        	  
        	   String dominioLogin = (String) session.getAttribute("dominio");
               Optional<Dominio> dominio = domMag.getDominioByName(dominioLogin);
              if(!dominio.isPresent()) {
            	  
            	  log.info("el dominio no existe");
            	  
            	  response.sendRedirect("/configapp/login?errorDominio=true" );
            	  
              }else {
            	  
            	  if(usuarioDB.getDomid().getId().equals(dominio.get().getId())) {
            		  response.sendRedirect("/configapp/menuzoomer" );
            		  
            	  }else {
            		  
            		  log.info("el dominio no coincide");
            		  response.sendRedirect("/configapp/login?errorDominio=true" );
            	  }
              }
        	   
           }else {
        	   
        	   response.sendRedirect("/configapp/menuzoomer" ); 
           }
//        	   
//        	   
//           }
            
            
            
        }
        

	}

}
