package cestel.sercom.web.vm;

import java.util.HashMap;
import java.util.Locale;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.lang.Library;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import cestel.sercom.web.service.ConexionSercomManager;
import cestel.sercom.web.service.UserManager;
import cestel.sercom.web.vm.bean.RolesAppBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class MenuVm {
	
	@WireVariable
	private UserManager userMag;
	
	@WireVariable
	private ConexionSercomManager conserMag;

	private String usuario;

	private String rolusuario;

	private RolesAppBean rolesbean;

	private String permisorol;

	private HashMap<String, String> rolesmap;

	private HashMap<String, String> permisosmap;

	private User user;

	

	private cestel.sercom.web.entity.User userLogin;

	@Init
	public void init() {

		Labels.reset();
		rolesbean = new RolesAppBean();
		rolesmap = new HashMap<String, String>();
		permisosmap = new HashMap<String, String>();
		user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		permisosRoles(permisosmap);
		cargaRoles(rolesmap);
		ifAllGranted();
		Session sess = Sessions.getCurrent();
		cestel.sercom.web.entity.User userLoginSession = (cestel.sercom.web.entity.User) sess
				.getAttribute("connectedUser");

		this.rolusuario = userLoginSession.getProfilecode().getDescripcion();

		// set lang locale
		Library.setProperty(Attributes.PREFERRED_LOCALE, "es");

	}

	/**
	 * Change the current application language to French and do the redirect
	 */
	@Command
	public void langEs() {
		final Locale prefer_locale = new Locale("es");
		Sessions.getCurrent().setAttribute(Attributes.PREFERRED_LOCALE, prefer_locale);
		Executions.getCurrent().sendRedirect("");
	}

	/**
	 * Change the current application language to English and do the redirect
	 */
	@Command
	public void langEn() {
		final Locale prefer_locale = new Locale("en");
		Sessions.getCurrent().setAttribute(Attributes.PREFERRED_LOCALE, prefer_locale);
		Executions.getCurrent().sendRedirect("");
	}

	private void ifAllGranted() {

		rolesmap.keySet().forEach(name -> {
			String minimoperfil = rolesmap.get(name);

			if (user.getAuthorities().stream().anyMatch(f -> f.getAuthority().equals(permisosmap.get(minimoperfil)))) {
				if (name.equals("Configuracion"))
					rolesbean.setMenu_configuracion("true");
				if (name.equals("Informes"))
					rolesbean.setMenu_estadisticas("true");
				if (name.equals("Monitorizacion"))
					rolesbean.setMenu_monitorizaciones("true");
				if (name.equals("Grabaciones"))
					rolesbean.setMenu_grabacion("true");
				if (name.equals("Webcall"))
					rolesbean.setMenu_webCall("true");
				if (name.equals("Marcador"))
					rolesbean.setMenu_marcador("true");
				if (name.equals("Ccdloader"))
					rolesbean.setMenu_ccdloader("true");
				if (name.equals("Webagente"))
					rolesbean.setMenu_webagent("true");
				if (name.equals("Estadisticas"))
					rolesbean.setMenu_dashboar("true");
				if (name.equals("Viapp"))
					rolesbean.setMenu_viapp("true");
			}
		});
	}

	private void permisosRoles(HashMap<String, String> permisosmap2) {
		permisosmap2.put("AGENT", "ROLE_100");
		permisosmap2.put("SUPERVISOR", "ROLE_200");
		permisosmap2.put("DOM_ADMIN", "ROLE_300");
		permisosmap2.put("SYS_ADMIN", "ROLE_400");
		permisosmap2.put("100", "AGENT");
		permisosmap2.put("200", "SUPERVISOR");
		permisosmap2.put("300", "DOM_ADMIN");
		permisosmap2.put("400", "SYS_ADMIN");

	}

	private void cargaRoles(HashMap<String, String> rolesmap2) {
		rolesmap2.put("Configuracion", Labels.getLabel("LoginApp.MinimumProfileForConfigApp"));
		rolesmap2.put("Informes", Labels.getLabel("LoginApp.MinimumProfileForStatsApp"));
		rolesmap2.put("Monitorizacion", Labels.getLabel("LoginApp.MinimumProfileForMonApp"));
		rolesmap2.put("Grabaciones", Labels.getLabel("LoginApp.MinimumProfileForRecApp"));
		rolesmap2.put("Webcall", Labels.getLabel("LoginApp.MinimumProfileForWebCall"));
		rolesmap2.put("Marcador", Labels.getLabel("LoginApp.MinimumProfileForDialerApp"));
		rolesmap2.put("Ccdloader", Labels.getLabel("LoginApp.MinimumProfileForCCDLoaderApp"));
		rolesmap2.put("Webagente", Labels.getLabel("LoginApp.MinimumProfileForWebAgentApp"));
		rolesmap2.put("Estadisticas", Labels.getLabel("LoginApp.MinimumProfileForDashboard"));
		rolesmap2.put("Viapp", Labels.getLabel("LoginApp.MinimumProfileForViApp"));

	}
}
