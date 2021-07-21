package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xml.sax.SAXException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import cestel.sercom.web.descriptor.bean.DeviceDescriptorBean;
import cestel.sercom.web.entity.Addins;
import cestel.sercom.web.entity.AddinsDev;
import cestel.sercom.web.entity.AddinsProp;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.service.AddinsDevManager;
import cestel.sercom.web.service.AddinsManager;
import cestel.sercom.web.service.DescriptorManager;
import cestel.sercom.web.service.PropAddinsManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CreateDeviceVm {

	@WireVariable
	private DescriptorManager decriptorMag;

	@WireVariable
	private AddinsManager addinsMag;

	@WireVariable
	private PropAddinsManager propaddinsMag;

	@WireVariable
	private AddinsDevManager addinsDevMag;

	DeviceDescriptorBean deviceDescriptorBean;
	private List<DeviceDescriptorBean> deviceDescriptorBeanList = new ArrayList<DeviceDescriptorBean>();

	private String source;

	Addins addins;

	AddinsDev addinsDev;

	AddinsProp addinsProp;
	User userLoginSession = null;
	Session session = Sessions.getCurrent();

	private String name;

	boolean createDNCheck = true;

	@Init
	public void init() throws SAXException {
		log.info("init CreateDeviceVm");
		userLoginSession = (User) session.getAttribute("connectedUser");
		final Execution execution = Executions.getCurrent();

		// estos son los atributos que vienen desde la creaccion de la ventana
		this.source = (String) execution.getArg().get("source");
		// this.addins = (Addins) execution.getArg().get("addins");
		// deviceVmBean = new DeviceVmBean();

		if (addins == null)
			deviceDescriptorBeanList = decriptorMag.cargaDescriptorDeviceType(source);

	}

	@Command
	public void onClose(@BindingParam("wnd") Window win) {
		win.detach();
	}

	@Command
	public void onSave(@BindingParam("wnd") Window win) throws SAXException {

		if (createDNCheck)
			System.out.println("true");
		else
			System.out.println("false");

		addins = new Addins();

		addins.setFamily(deviceDescriptorBean.getFamily());
		addins.setType(deviceDescriptorBean.getType());
		addins.setVersion(deviceDescriptorBean.getVersion());
		addins.setAclass("D");
		addins.setNodeid(Labels.getLabel("CfgApp.node"));
		addins = addinsMag.saveOrUpdate(addins);

		addinsDev = new AddinsDev();
		addinsDev.setId(addins.getId());
		addinsDev.setName(name);
		addinsDev.setDevgroup(source);
		addinsDev.setMedia(deviceDescriptorBean.getMedia().substring(0, 1));
		addinsDevMag.saveOrUpdate(addinsDev);

		propaddinsMag.deleteByAddins(addins);

		Map<String, String> propDescriptor = decriptorMag.cargaDescriptorPropDevice(addins, source);

		for (Map.Entry<String, String> entry : propDescriptor.entrySet()) {

			addinsProp = new AddinsProp();

			if (entry.getKey() != null)

				if (entry.getValue() != null) {
					addinsProp.setAddins(addins);

					addinsProp.setValue(entry.getValue());
					addinsProp.setName(entry.getKey());
				}
			addinsProp = propaddinsMag.saveOrUpdate(addinsProp);
		}
		BindUtils.postGlobalCommand(null, null, "loadDevices", null);
		win.detach();
	}

	@Command
	public void deviceSelect(@BindingParam("device") DeviceDescriptorBean device) throws SAXException {
		// System.out.println(device.getType());
		this.deviceDescriptorBean = device;
	}

	@Command
	public void onSelect() {

		if (createDNCheck)
			createDNCheck = false;

		else
			createDNCheck = true;
		System.out.println(createDNCheck);
	}

}
