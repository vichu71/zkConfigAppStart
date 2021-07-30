package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.xml.sax.SAXException;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;

import cestel.sercom.web.entity.AddinsDev;
import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.service.DescriptorManager;
import cestel.sercom.web.service.DominioManager;
import cestel.sercom.web.util.ComboDto;
import cestel.sercom.web.util.NavigationPage;
import cestel.sercom.web.vm.bean.DnsVmBean;
import cestel.sercom.web.vm.bean.DominioVmBean;
import cestel.sercom.web.vm.bean.SubDominioVmBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class SelectDominioVm {

	
	private List<Dominio> dominioList = new ArrayList<Dominio>();
	
	DnsVmBean dnsVmBean = new DnsVmBean();

	private DominioVmBean dominioVmBean;
	
	@WireVariable
	private DominioManager domMag;

	@Init
	public void init() {

		log.info("Init SubDominioVM");

		
		dominioVmBean = new DominioVmBean();
		dominioList =	domMag.getAllDominio();
		loadComboDominios();

		// initialize filter to null (no value)
		
	}
	private void loadComboDominios() {
		dominioList.stream().forEach(dom -> {
			dominioVmBean.getListDominios().add(new ComboDto(dom.getIdString(), dom.getName()));

		});

	}
	@Command
	public void onClose(@BindingParam("wnd") Window win) {
		win.detach();
	}
	
	
}
