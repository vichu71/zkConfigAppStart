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
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.service.DescriptorManager;
import cestel.sercom.web.util.ComboDto;
import cestel.sercom.web.util.NavigationPage;
import cestel.sercom.web.vm.bean.DnsVmBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class SelectGroupVm {

	@WireVariable
	private DescriptorManager decriptorMag;
	List<String> devicesMenu = new ArrayList<String>();
	Map<String, String> valueGroupmap;
	DnsVmBean dnsVmBean = new DnsVmBean();

	@Init
	public void init() throws SAXException {
		devicesMenu = decriptorMag.cargaDescriptorAddinsMenu();
		mapDnsVm();
	}

	private void mapDnsVm() {
		devicesMenu.stream().forEach(grupo -> {
			dnsVmBean.getListDevice().add(new ComboDto(grupo, grupo));

		});

	}

	@Command
	public void selectGroup( @BindingParam("dnsBean") DnsVmBean dnsBean, @BindingParam("wnd") Window wDns) {
		Window window = null;
		final HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("source", dnsBean.getDeviceComboSelecionado().getLabel());
        window = (Window) Executions.createComponents("~./zul/createdevice.zul", null, map);
		window.doModal();
		wDns.detach();

	}
	@Command
	public void onClose(@BindingParam("wnd") Window win) {
		win.detach();
	}
	
	
}
