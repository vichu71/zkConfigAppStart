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
import org.zkoss.util.resource.Labels;
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

import cestel.sercom.web.descriptor.bean.ResOptionsBean;
import cestel.sercom.web.entity.AddinsDev;
import cestel.sercom.web.entity.Dns;
import cestel.sercom.web.entity.Dominio;
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
import cestel.sercom.web.vm.bean.DominioVmBean;
import cestel.sercom.web.vm.bean.UserVmBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditDominioVm {

	
	@WireVariable
	private DominioManager domMag;

	DominioVmBean dominioVmBean;

	
	
	Dominio dominio = new Dominio();
	Map<String, String> valueLabelmap;
	User userLoginSession = null;
	Session session = Sessions.getCurrent();
	final Execution execution = Executions.getCurrent();
	private List<Dominio> dominioCheck;

	@Init
	public void init() {
//		recupero el usuario de sesion
		userLoginSession = (User) session.getAttribute("connectedUser");

		// recupero los parametros de la vista padre que envia por el map
		this.dominioCheck = (List<Dominio>) execution.getArg().get("dominioCheck");
		this.dominio = (Dominio) execution.getArg().get("dominio");
		mapDominioVm();

	}

	
	@Command
	public void onClose(@BindingParam("wnd") Window win) {
		win.detach();
	}

	@Command
	public void saveOrUpdateDominio(Event e, @BindingParam("dominioBean") DominioVmBean dominioBean, @BindingParam("wnd") Window wDominio)
			throws InterruptedException {
		 Dominio  dominio = mapDominioBeanToDominio(dominioBean);

		if (dominioCheck != null) {
			dominioCheck.stream().forEach(domain -> {

				dominio.setId(domain.getId());
				dominio.setName(domain.getName());
				domMag.saveOrUpdate(dominio);
			});
		} else {
			domMag.saveOrUpdate(dominio);

		}
		// actualizo las variables de la vista padre.
		BindUtils.postGlobalCommand(null, null, "loadDominio", null);
		BindUtils.postGlobalCommand(null, null, "loadDominioCheck", null);
		wDominio.detach();
		if (dominioBean.getId() != null)
			ApplicationUtils.showInfo("message.dnsModificado");
		else
			ApplicationUtils.showInfo("message.dnsCreado");

	}

	private Dominio mapDominioBeanToDominio(DominioVmBean dominioBean) {
		Dominio dominio = new Dominio();
		if (dominioBean != null)
			dominio.setId(dominioBean.getId());
		dominio.setName(dominioBean.getName());
		dominio.setInfo(dominioBean.getInfo());
		dominio.setC_schema(dominioBean.getSchema());
		dominio.setUtc(dominioBean.getUtc());
		
		return dominio;
	}

	private void mapDominioVm() {

		dominioVmBean = new DominioVmBean();

		

		this.dominio = (Dominio) execution.getArg().get("dominio");

		if (dominio != null) {
			dominioVmBean.setId(dominio.getId());
			dominioVmBean.setName(dominio.getName());
			dominioVmBean.setInfo(dominio.getInfo());
			dominioVmBean.setSchema(dominio.getC_schema());
			dominioVmBean.setUtc(dominio.getUtc());
		}
	}

}
