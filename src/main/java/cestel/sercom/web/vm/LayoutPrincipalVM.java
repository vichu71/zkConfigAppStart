package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;

import cestel.sercom.web.service.DescriptorManager;
import cestel.sercom.web.util.NavigationPage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class LayoutPrincipalVM extends Window {
	@WireVariable
	NavigationPage currentPage;
	private Map<String, Map<String, NavigationPage>> pageMap;

//	  Session sess = Sessions.getCurrent();
//	  Execution exec = Executions.getCurrent();
//	  String source=exec.getParameter("resource");
	@WireVariable
	private DescriptorManager decriptorMag;
	List<String> devicesMenu = new ArrayList<String>();

	@Init
	public void init() throws SAXException {
		devicesMenu = decriptorMag.cargaDescriptorAddinsMenu();
		initPageMap();
		// currentPage = pageMap.get("Usuarios").get("List");
	}

	@Command
	public void navigatePage(@BindingParam("target") NavigationPage targetPage) {
		BindUtils.postNotifyChange(null, null, currentPage, "selected");
		currentPage = targetPage;
		BindUtils.postNotifyChange(null, null, this, "currentPage");
	}
//	@Command
//	@NotifyChange("pageMap")
//	public void refreshDevice() {
//		log.info("refreshDevice... ");
//		try {
//			devicesMenu = decriptorMag.cargaDescriptorAddinsMenu();
//		} catch (SAXException e) {
//			log.error("Buscando Descriptores XML... ");
//			e.printStackTrace();
//		}
//		initPageMap();
//	}
	//@NotifyChange("pageMap")
	private void initPageMap() {
		pageMap = new LinkedHashMap<String, Map<String, NavigationPage>>();

		addPage("Resources", "Services", "listadoresource.zul", "S");
		addPage("Resources", "Channels", "listadoresource.zul", "C");
		addPage("Resources", "Calendars", "listadoCalendars.zul", "L");
		addPage("Resources", "Queues", "listadoresource.zul", "Q");
		addPage("Resources", "Announcements", "listadoannouncement.zul", "A");
		addPage("Resources", "Answers", "listadoresource.zul", "A");
		addPage("Resources", "Vectors", "listadoVectors.zul", "V");
		addPage("Resources", "Teams", "listadoresource.zul", "T");
		addPage("Resources", "Alarms", "listadoresource.zul", "R");

		addPage("Usuarios", "List", "listadousuarios.zul");
		addPage("Usuarios", "Associations", "asociacionusuarios.zul");

		addPage("Directory", "List", "listadodns2.zul");
		addPage("Directory", "Dial rules", "listadoDalRules.zul");
		addPage("Directory", "Subdomains", "listadoSubDomains.zul");
		addPage("Directory", "Domains", "listadoDomains.zul");
		addPage("Directory", "Sites", "listadoSites.zul");

		devicesMenu.stream().forEach(dev -> {
			addPage("Devices", dev, "listadoDevice.zul", dev);

		});
		addPage("Plugins", "TechPlugins", "listadoTechPlugins.zul");
		addPage("Plugins", "ClientPlugins", "listadoClientPlugins.zul");

		addPage("Tools", "Inbound wizard", "listadoInboundWizard.zul");
		addPage("Tools", "Events logs", "listadoEventsLogs.zul");
		addPage("Tools", "Licenses", "listadoLicenses.zul");
		addPage("Tools", "Debugger", "listadoDebugger.zul");
		addPage("Tools", "System Properties", "listadoSystemproperties.zul");

		addPage("Help", "Contact", "contact.zul");

	}

	private void addPage(String title, String subTitle, String includeUri) {
		addPage(title, subTitle, includeUri, null);
	}

	private void addPage(String title, String subTitle, String includeUri, String data) {

//	    	 sess.setAttribute("source",data);
		String folder = "/zul/";
		Map<String, NavigationPage> subPageMap = pageMap.get(title);
		if (subPageMap == null) {
			subPageMap = new LinkedHashMap<String, NavigationPage>();
			pageMap.put(title, subPageMap);
		}
		NavigationPage navigationPage = new NavigationPage(title, subTitle,
				folder + includeUri + "?random=" + Math.random(), data) {
			@Override
			public boolean isSelected() {
				return currentPage == this;
			}
		};
		subPageMap.put(subTitle, navigationPage);
	}

}
