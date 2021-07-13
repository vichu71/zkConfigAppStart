package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.xml.sax.SAXException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.DropEvent;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cestel.sercom.web.descriptor.bean.DeviceDescriptorBean;
import cestel.sercom.web.descriptor.bean.ResClassPropBean;
import cestel.sercom.web.descriptor.bean.ResOptionsBean;
import cestel.sercom.web.entity.Addins;
import cestel.sercom.web.entity.PropResource;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.exception.CxException;
import cestel.sercom.web.service.ConexionSercomManager;
import cestel.sercom.web.service.DescriptorManager;
import cestel.sercom.web.service.PropResourceManager;
import cestel.sercom.web.service.ResourceManager;
import cestel.sercom.web.service.imp.ResourceXMLImpl;
import cestel.sercom.web.vm.bean.DeviceVmBean;
import cestel.sercom.web.vm.bean.ResourceVmBean;
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

	DeviceDescriptorBean deviceDescriptorBean;
	private List<DeviceDescriptorBean> deviceDescriptorBeanList = new ArrayList<DeviceDescriptorBean>();
	
	
	private String source;
	Addins addins;

	PropResource propresource;
	User userLoginSession = null;
	Session session = Sessions.getCurrent();

	
	@Init
	public void init() throws SAXException {
		log.info("init CreateDeviceVm");
		userLoginSession = (User) session.getAttribute("connectedUser");
		final Execution execution = Executions.getCurrent();

		// estos son los atributos que vienen desde la creaccion de la ventana
		this.source = (String) execution.getArg().get("source");
		this.addins = (Addins) execution.getArg().get("addins");
		//	deviceVmBean = new DeviceVmBean();
		
		if (addins == null)
			deviceDescriptorBeanList = decriptorMag.cargaDescriptorDeviceType(source);

	}


	@Command
	public void onClose(@BindingParam("wnd") Window win) {
		win.detach();
	}

	@Command
	public void onSave() {

//		log.info(getName().getValue());

	}
}
