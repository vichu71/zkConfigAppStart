package cestel.sercom.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import lombok.extern.slf4j.Slf4j;

/**
 * This is an example of minimal configuration for ZK + Spring Security, we open as less access as possible to run a ZK-based application.
 * Please understand the configuration and modify it upon your requirement.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ZUL_FILES = "/zkau/web/**/**/*.zul";

    private static final String[] ZK_RESOURCES = {
            "/include/**",
            "/css/**",
            "/icons/**",
            "/img/**",
            "/js/**",
            "/zkau/web/**/js/**",
            "/zkau/web/**/css/**",
            "/zkau/web/**/font/**",
            "/zkau/web/**/img/**",
            "/zkau/web/**/zul/img/**"
    };
    // allow desktop cleanup after logout or when reloading login page
    //private static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";

    @Autowired
    UserService userDetailsService;
    @Autowired
    private CustomAuthenticationSuccesHandler customAuthenticationSuccessHandler;
    @Autowired
    BCryptPasswordEncoder bcrypt;
    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(bcrypt);
    } 
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
    	
    	http.authorizeRequests()
        .antMatchers(HttpMethod.GET, ZK_RESOURCES).permitAll()
        .antMatchers(HttpMethod.GET, ZUL_FILES).denyAll()
        
        .mvcMatchers("/","/login","/logout").permitAll()
       // .mvcMatchers("/menuzoomer").hasRole("100")
        
        //.antMatchers("/zkau/web/**/**.zul").denyAll() 
       // .antMatchers("/zkau/web/**/**/**.zul").denyAll()// calling a zul-page directly is not allowed -- should we put this in the auto-configuration to? --
       // .anyRequest().authenticated()
      .and()
      //cuando logeamos si ha ido ok, pasamos el usuario a la session con customAuthenticationSuccessHandler
        .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/menuzoomer").successHandler(customAuthenticationSuccessHandler).failureUrl("/login?error=true")
        .usernameParameter("username")
        .passwordParameter("password")
        
      .and()  
        .logout().permitAll().logoutUrl("/logout").logoutSuccessUrl("/");
    	
    	
    }
    /**
     * Creates an {@link InMemoryUserDetailsManager} for demo/testing purposes only. DON'T use this in production, see: {@link User#withUserDetails}!
     * @return userDetailsService
     */

    @Bean
	public BCryptPasswordEncoder passwordEncoder() {
    	
    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // Strength set as 12
    	String encodedPassword = encoder.encode("1211");
    	log.info("1211-> "+encodedPassword);
	    return encoder;
	}
    
}