package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.select.impl.Selector;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.Roles;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.service.DominioManager;
import cestel.sercom.web.service.ResourceManager;
import cestel.sercom.web.service.RolesManager;
import cestel.sercom.web.service.SubDominioManager;
import cestel.sercom.web.service.UserManager;
import cestel.sercom.web.util.ApplicationUtils;
import cestel.sercom.web.vm.bean.UserVmBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditUsuarioVm  extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	UserVmBean userVmBean;

	@Wire
	private User usuario;
	
	@Wire
	private Combobox cmbTeam;

	List<Dominio> dominio =new ArrayList<>();

	List<SubDominio> subDominio =new ArrayList<>();

	List<Roles> roles =new ArrayList<>();

	List<Resource> resource =new ArrayList<>();

	List<String> dominioname =new ArrayList<>();

	List<String> subdominioname =new ArrayList<>();

	List<String> rolesname =new ArrayList<>();

	List<String> teamname =new ArrayList<>();

	@WireVariable
	private DominioManager domMag;

	@WireVariable
	private SubDominioManager subDomMag;

	@WireVariable
	private RolesManager rolesMag;

	@WireVariable
	private ResourceManager resourceMag;

	@WireVariable
	private UserManager userMag;

	@Wire
	Window wUsuarios;

	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Init
	public void init() {

		mapUserVm();

		cargaCombos();
		
	}
	@AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(view, this, false);
        //cmbTeam.setModel(listTeam);
    }

	@Command
	@NotifyChange("teamname")
	public void showCreateTeam(@BindingParam("usuario") UserVmBean usuario) {
		Window window = null;
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (usuario != null) {
			map.put("usuario", usuario);
			map.put("source", "T");
			window = (Window) Executions.createComponents("~./zul/appconfig/editarresource.zul", null, map);
		} 

		window.doModal();
		// UIUtils.show("editarusuario", null, null);
	}


	@Command
	public void onClose(@BindingParam("wnd") Window win) {
		win.detach();
	}

	@Command
	public void saveOrUpdateUser(Event e, @BindingParam("userBean") UserVmBean userBean,
			@BindingParam("wnd") Window wUsuarios) throws InterruptedException {
		User user = mapUserBeanToUser(userBean);
		userMag.saveOrUpdate(user);
		BindUtils.postGlobalCommand(null, null, "loadUsuarios", null);
		
		wUsuarios.detach();
		if (userBean.getId() != null)
			ApplicationUtils.showInfo("message.usuarioModificado");
		else
			ApplicationUtils.showInfo("message.usuarioCreado");

	}

	private User mapUserBeanToUser(UserVmBean userVmBean) {
		User user = new User();
		if (userVmBean != null)
			user.setId(userVmBean.getId());
		user.setName(userVmBean.getName());
		user.setInfo(userVmBean.getInfo());
		user.setProfilecode(
				roles.stream().filter(namerol -> namerol.getDescripcion().equals(userVmBean.getProfilecode()))
						.findFirst().orElseThrow());

		if (!"-all-".equals(userVmBean.getDominio()))
			user.setDomid(dominio.stream().filter(dominio -> dominio.getName().equals(userVmBean.getDominio()))
					.findFirst().orElseThrow());
		else
			user.setDomid(null);
		if (!"-all-".equals(userVmBean.getSubdomid()))
			user.setSubdomid(
					subDominio.stream().filter(subDominio -> subDominio.getName().equals(userVmBean.getSubdomid()))
							.findFirst().orElseThrow());
		else
			user.setSubdomid(null);
		user.setUsername(userVmBean.getUsername());
		user.setPassword(encoder.encode(userVmBean.getPassword()));

		if (!"-none-".equals(userVmBean.getTeam()))
			user.setResource(resource.stream().filter(resource -> resource.getName().equals(userVmBean.getTeam()))
					.findFirst().orElseThrow());
		else
			user.setResource(null);
		return user;
	}

	private void mapUserVm() {

		userVmBean = new UserVmBean();
		final Execution execution = Executions.getCurrent();

		this.usuario = (User) execution.getArg().get("usuario");
		if (usuario != null) {
			userVmBean.setId(usuario.getId());
			userVmBean.setName(usuario.getName());
			userVmBean.setInfo(usuario.getInfo());
			userVmBean.setProfilecode(usuario.getProfilecode().getDescripcion());
			if (usuario.getDomid() != null)
				userVmBean.setDominio(usuario.getDomid().getName());
			if (usuario.getSubdomid() != null)
				userVmBean.setSubdomid(usuario.getSubdomid().getName());
			userVmBean.setUsername(usuario.getUsername());
			
			userVmBean.setPassword(usuario.getPassword());
			
			if (usuario.getResource() != null)
				userVmBean.setTeam(usuario.getResource().getName());
		}
	}
	
	private void cargaCombos() {

		dominio = domMag.getAllDominio();
		subDominio = subDomMag.getAllSubDominio();
		roles = rolesMag.getAllRoles();
		dominioname = dominio.stream().map(Dominio::getName).collect(Collectors.toList());
		subdominioname = subDominio.stream().map(SubDominio::getName).collect(Collectors.toList());
		rolesname = roles.stream().map(Roles::getDescripcion).collect(Collectors.toList());
		resource = resourceMag.getResourcebyClassAndSubdominio("T", subDominio.get(0));// buscar el subdomninio como OBJ
		this.teamname = resource.stream().map(Resource::getName).collect(Collectors.toList());

		dominioname.add(0, "-all-");
		subdominioname.add(0, "-all-");
		teamname.add(0, "-none-");


	}
	
	
	@Command
	@NotifyChange("*")
	public void refreshTeamModel() {
		resource = resourceMag.getResourcebyClassAndSubdominio("T", subDominio.get(0));
		this.teamname = resource.stream().map(Resource::getName)
				.collect(Collectors.toList());

		teamname.add(0, "-none-");

		//cmbTeam.setSelectedIndex(teamname.size()-1);
	}

}
