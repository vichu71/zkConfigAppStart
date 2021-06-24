package cestel.sercom.web.security;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import cestel.sercom.web.service.UserManager;
import lombok.Getter;


@Service
@Transactional
public class UserService implements UserDetailsService {
	 
	
	@Getter
    private cestel.sercom.web.entity.User userLogin;
	
	@Autowired
    private UserManager userMag;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		this.userLogin= userMag.getLogin(username).orElseThrow(() -> new UsernameNotFoundException("Username does not exist"));
		return this.userBuilder(userLogin.getUsername(), userLogin.getPassword(), userLogin.getProfilecode().getId().toString());
	}
	private User userBuilder(String username,String password, String profilecode) {
		
		
		List<GrantedAuthority> roles	= new ArrayList<>();
		
		//roles segun bbdd
		/*  100 Agente-> solo ve lo suyo
			200 supervisor-> ve lo suyo y agente
		    300 DOM_ADMIN - ve lo suyo, supervisor y agente
		    400 SYS_ADMIN -> ve todo
		    
		    comcatenado con ROLE_ por que asi lo pide la configuracion de sprintsecurity*/
		
		if(profilecode.equals("400")) {
			roles.add(new SimpleGrantedAuthority("ROLE_100"));
			roles.add(new SimpleGrantedAuthority("ROLE_200"));
			roles.add(new SimpleGrantedAuthority("ROLE_300"));
			roles.add(new SimpleGrantedAuthority("ROLE_400"));
			
		}else if(profilecode.equals("300")) {
			roles.add(new SimpleGrantedAuthority("ROLE_300"));
			roles.add(new SimpleGrantedAuthority("ROLE_200"));
			roles.add(new SimpleGrantedAuthority("ROLE_100"));
			
		}else if(profilecode.equals("200")) {
			roles.add(new SimpleGrantedAuthority("ROLE_200"));
			roles.add(new SimpleGrantedAuthority("ROLE_100"));
			
		}else if(profilecode.equals("100")) {
			roles.add(new SimpleGrantedAuthority("ROLE_100"));
			
		}
		
		
		return new User(username,password,roles);
	}

}
