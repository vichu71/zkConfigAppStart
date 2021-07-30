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

import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.service.SubDominioManager;
import cestel.sercom.web.util.ApplicationUtils;
import cestel.sercom.web.vm.bean.SubDominioVmBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditSubDominioVm {

	
	@WireVariable
	private SubDominioManager subDomMag;

	private SubDominioVmBean subDominioVmBean;

	
	
	SubDominio subDominio = new SubDominio();
	Map<String, String> valueLabelmap;
	User userLoginSession = null;
	Session session = Sessions.getCurrent();
	final Execution execution = Executions.getCurrent();
	private List<SubDominio> subDominioCheck;

	@Init
	public void init() {
		log.info("Init-> " + EditSubDominioVm.class.getName());
		userLoginSession = (User) session.getAttribute("connectedUser");

		// recupero los parametros de la vista padre que envia por el map
		this.subDominioCheck = (List<SubDominio>) execution.getArg().get("subDominioCheck");
		this.subDominio = (SubDominio) execution.getArg().get("subDominio");
		mapSubDominioVm();

	}

	
	@Command
	public void onClose(@BindingParam("wnd") Window win) {
		win.detach();
	}

	@Command
	public void saveOrUpdateSubDominio(Event e, @BindingParam("subDominioBean") SubDominioVmBean subDominioBean, @BindingParam("wnd") Window wSubDominio)
			throws InterruptedException {
		 SubDominio  subDominio = mapSubDominioBeanToSubDominio(subDominioBean);

		if (subDominioCheck != null) {
			subDominioCheck.stream().forEach(domain -> {

				subDominio.setId(domain.getId());
				subDominio.setName(domain.getName());
				subDominio.setDominio(subDominioBean.getDominio());
				subDominio.setUtc(subDominioBean.getUtc());
				subDomMag.saveOrUpdate(subDominio);
			});
		} else {
			subDomMag.saveOrUpdate(subDominio);

		}
		// actualizo las variables de la vista padre.
		BindUtils.postGlobalCommand(null, null, "loadSubDominios", null);
		BindUtils.postGlobalCommand(null, null, "loadSubDominiosCheck", null);
		wSubDominio.detach();
		if (subDominioBean.getId() != null)
			ApplicationUtils.showInfo("message.dnsModificado");
		else
			ApplicationUtils.showInfo("message.dnsCreado");

	}

	private SubDominio mapSubDominioBeanToSubDominio(SubDominioVmBean subDominioBean) {
		SubDominio subDominio = new SubDominio();
		if (subDominioBean != null) {
			subDominio.setId(subDominioBean.getId());
			subDominio.setDominio(userLoginSession.getDomid());
		}else {
			
			subDominio.setDominio(subDominioBean.getDominio());
		}
		subDominio.setName(subDominioBean.getName());
		subDominio.setInfo(subDominioBean.getInfo());
		
		subDominio.setUtc(subDominioBean.getUtc());
		
		return subDominio;
	}

	private void mapSubDominioVm() {

		subDominioVmBean = new SubDominioVmBean();

		

		this.subDominio = (SubDominio) execution.getArg().get("subDominio");

		if (subDominio != null) {
			subDominioVmBean.setId(subDominio.getId());
			subDominioVmBean.setName(subDominio.getName());
			subDominioVmBean.setInfo(subDominio.getInfo());
			subDominioVmBean.setDominio(subDominio.getDominio());
			subDominioVmBean.setUtc(subDominio.getUtc());
		}
	}

}
