package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.select.impl.Selector;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cestel.sercom.web.entity.AddinsDev;
import cestel.sercom.web.entity.Dns;
import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.entity.ResOptions;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.Roles;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.service.AddinsDevManager;
import cestel.sercom.web.service.DnsManager;
import cestel.sercom.web.service.DominioManager;
import cestel.sercom.web.service.ResourceManager;
import cestel.sercom.web.service.RolesManager;
import cestel.sercom.web.service.SubDominioManager;
import cestel.sercom.web.service.UserManager;
import cestel.sercom.web.util.ApplicationUtils;
import cestel.sercom.web.util.ComboDto;
import cestel.sercom.web.vm.bean.DnsVmBean;
import cestel.sercom.web.vm.bean.UserVmBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditDnsVm {

	@WireVariable
	private AddinsDevManager addinsDevMag;

	@WireVariable
	private DnsManager dnsMag;

	DnsVmBean dnsVmBean;

	List<AddinsDev> devicesList = new ArrayList<>();

	List<String> devicesName = new ArrayList<>();

	// AddinsDev device;
	Dns dns = new Dns();
	Map<String, String> valueLabelmap;
	User userLoginSession = null;
	Session session = Sessions.getCurrent();
	final Execution execution = Executions.getCurrent();
	private List<Dns> dnsCheck;

	@Init
	public void init() {
//		recupero el usuario de sesion
		userLoginSession = (User) session.getAttribute("connectedUser");
		
		//recupero los parametros de la vista padre que envia por el map
		this.dnsCheck = (List<Dns>) execution.getArg().get("dnsCheck");
		mapDnsVm();

		// cargaCombos();

	}

	@Command
	@NotifyChange("teamname")
	public void showCreateTeam(@BindingParam("usuario") UserVmBean usuario) {
		Window window = null;
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (usuario != null) {
			map.put("usuario", usuario);
			map.put("source", "T");
			window = (Window) Executions.createComponents("/zul/editarresource.zul", null, map);
		}

		window.doModal();
		// UIUtils.show("editarusuario", null, null);
	}

	@Command
	public void onClose(@BindingParam("wnd") Window win) {
		win.detach();
	}

	@Command
	public void saveOrUpdateDns(Event e, @BindingParam("dnsBean") DnsVmBean dnsBean, @BindingParam("wnd") Window wDns)
			throws InterruptedException {
		Dns dns = mapDnsBeanToDns(dnsBean);
		
		if(dnsCheck!=null) {
			dnsCheck.stream().forEach(dn->{
				
				dns.setId(dn.getId());
				dns.setName(dn.getName());
				dnsMag.saveOrUpdate(dns);});
		}else {
			dnsMag.saveOrUpdate(dns);
			
		}
		//actualizo las variables de la vista padre.
		BindUtils.postGlobalCommand(null, null, "loadDns", null);
		BindUtils.postGlobalCommand(null, null, "loadDnsCheck", null);
		wDns.detach();
		if (dnsBean.getId() != null)
			ApplicationUtils.showInfo("message.dnsModificado");
		else
			ApplicationUtils.showInfo("message.dnsCreado");

	}

	private Dns mapDnsBeanToDns(DnsVmBean dnsBean) {
		Dns dns = new Dns();
		if (dnsBean != null)
			dns.setId(dnsBean.getId());
		dns.setName(dnsBean.getName());
		dns.setDomid(userLoginSession.getDomid());
		dns.setMediacode(dnsBean.getMediaComboSelecionado().getValue());
		dns.setDntypecode(dnsBean.getTypeComboSelecionado().getValue());
		dns.setRemotepeer(dnsBean.getPeer());
		dns.setAddinsDev(addinsDevMag.getAddinsDevbyId(Long.valueOf(dnsBean.getDeviceComboSelecionado().getValue())));
		dns.setNodeid("");

//		if (!"-none-".equals(dnsVmBean.getTeam()))
//			user.setResource(resource.stream().filter(resource -> resource.getName().equals(dnsVmBean.getTeam()))
//					.findFirst().orElseThrow());
//		else
//			user.setResource(null);
		return dns;
	}

	private void mapDnsVm() {

		dnsVmBean = new DnsVmBean();

		dnsVmBean.getListMedia().add(new ComboDto("V", "Voice"));
		dnsVmBean.getListMedia().add(new ComboDto("M", "e-Mail"));
		dnsVmBean.getListMedia().add(new ComboDto("C", "Chat"));
		dnsVmBean.getListMedia().add(new ComboDto("I", "Video"));

		dnsVmBean.getListType().add(new ComboDto("L", "Local"));
		dnsVmBean.getListType().add(new ComboDto("R", "Remote"));
		dnsVmBean.getListType().add(new ComboDto("S", "Service"));

		devicesList = addinsDevMag.getAll();

		valueLabelmap = devicesList.stream().collect(Collectors.toMap(AddinsDev::getIdString, AddinsDev::getName));

		for (Map.Entry<String, String> entry : valueLabelmap.entrySet()) {
			dnsVmBean.getListDevice().add(new ComboDto(entry.getKey(), entry.getValue()));
		}
		

		this.dns = (Dns) execution.getArg().get("dns");
		
		
		
		if (dns != null) {
			dnsVmBean.setId(dns.getId());
			dnsVmBean.setName(dns.getName());
			dnsVmBean.setMediaComboSelecionado(dnsVmBean.getListMedia().stream().filter(f->f.getValue().equals(dns.getMediacode())).findFirst().orElseThrow());
			dnsVmBean.setTypeComboSelecionado(dnsVmBean.getListType().stream().filter(f->f.getValue().equals(dns.getDntypecode())).findFirst().orElseThrow());
			dnsVmBean.setPeer(dns.getRemotepeer());
			
			dnsVmBean.setDeviceComboSelecionado(dnsVmBean.getListDevice().stream().filter(f->f.getLabel().equals(dns.getAddinsDev().getName())).findFirst().orElseThrow());
		}
	}

	private void cargaCombos() {

		devicesList = addinsDevMag.getAll();
//		this.devicesName = devicesList.stream().map(AddinsDev::getName)
//				.collect(Collectors.toList());
//
//		devicesName.add(0, "-none-");
		valueLabelmap = new HashMap<>();
//		valueLabelmap = devicesList.stream().collect(Collectors.toMap(AddinsDev::getId, AddinsDev::getName));

	}

	@Command
	@NotifyChange("*")
	public void refreshDeviceModel() {
		devicesList = addinsDevMag.getAll();
		this.devicesName = devicesList.stream().map(AddinsDev::getName).collect(Collectors.toList());

		devicesName.add(0, "-none-");

		// cmbTeam.setSelectedIndex(teamname.size()-1);
	}

}
