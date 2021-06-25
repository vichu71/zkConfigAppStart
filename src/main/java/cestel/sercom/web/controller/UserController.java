package cestel.sercom.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

//@PreAuthorize("authenticated")
@Controller
public class UserController {

	
  @GetMapping("/login")
  public String index() {
    return "index";
  }
  @PreAuthorize("authenticated")
  @GetMapping("/menuzoomer")
  public String menu( HttpSession session, Model model) {
    return "menuzoomer";
  }
  @GetMapping("/error")
  public String error() {
    return "error";
  }
  @PreAuthorize("authenticated")
  @GetMapping("/layoutprincipal")
  public String layoutprincipal() {
    return "appconfig/layoutprincipal";
  }
  

  @PreAuthorize("authenticated")
  @GetMapping("/botonesiconos")
  public String botonesiconos( HttpSession session, Model model) {
    return "botonesiconos";
  }
    
}
