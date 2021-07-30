package cestel.sercom.web.vm;

import java.util.List;
import java.util.Map;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import cestel.sercom.web.entity.Sites;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.service.SitesManager;
import cestel.sercom.web.util.ApplicationUtils;
import cestel.sercom.web.vm.bean.SitesVmBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditSitesVm {

	
	@WireVariable
	private SitesManager siteMag;

	SitesVmBean sitesVmBean;

	
	
	Sites sites = new Sites();
	Map<String, String> valueLabelmap;
	User userLoginSession = null;
	Session session = Sessions.getCurrent();
	final Execution execution = Executions.getCurrent();
	private List<Sites> sitesCheck;

	@Init
	public void init() {
//		recupero el usuario de sesion
		userLoginSession = (User) session.getAttribute("connectedUser");

		// recupero los parametros de la vista padre que envia por el map
		this.sitesCheck = (List<Sites>) execution.getArg().get("sitesCheck");
		this.sites = (Sites) execution.getArg().get("sites");
		mapSitesVm();

	}

	
	@Command
	public void onClose(@BindingParam("wnd") Window win) {
		win.detach();
	}

	@Command
	public void saveOrUpdateSites(Event e, @BindingParam("sitesBean") SitesVmBean sitesBean, @BindingParam("wnd") Window wSites)
			throws InterruptedException {
		 Sites  sites = mapSitesBeanToSites(sitesBean);

		if (sitesCheck != null) {
			sitesCheck.stream().forEach(domain -> {

				sites.setId(domain.getId());
				sites.setName(domain.getName());
				siteMag.saveOrUpdate(sites);
			});
		} else {
			siteMag.saveOrUpdate(sites);

		}
		// actualizo las variables de la vista padre.
		BindUtils.postGlobalCommand(null, null, "loadSites", null);
		BindUtils.postGlobalCommand(null, null, "loadSitesCheck", null);
		wSites.detach();
		if (sitesBean.getId() != null)
			ApplicationUtils.showInfo("message.dnsModificado");
		else
			ApplicationUtils.showInfo("message.dnsCreado");

	}

	private Sites mapSitesBeanToSites(SitesVmBean sitesBean) {
		Sites sites = new Sites();
		if (sitesBean != null)
			sites.setId(sitesBean.getId());
		sites.setName(sitesBean.getName());
		sites.setInfo(sitesBean.getInfo());
		
		
		return sites;
	}

	private void mapSitesVm() {

		sitesVmBean = new SitesVmBean();

		

		this.sites = (Sites) execution.getArg().get("sites");

		if (sites != null) {
			sitesVmBean.setId(sites.getId());
			sitesVmBean.setName(sites.getName());
			sitesVmBean.setInfo(sites.getInfo());
			
		}
	}

}
