package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cestel.sercom.web.entity.Addins;
import cestel.sercom.web.entity.Dns;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.service.AddinsManager;
import cestel.sercom.web.service.ResourceManager;
import cestel.sercom.web.service.imp.ResourceXMLImpl;
import cestel.sercom.web.util.ApplicationUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DeviceVm {

	private List<Addins> devices;

	private List<Addins> devicesCheck;

	// initialize filter to null (no value)
	private String filter;

	@WireVariable
	private AddinsManager addinsMag;
	@WireVariable
	private ResourceXMLImpl resourceXml;

	Grid grid1;
	Window wDevices;
	Textbox data = new Textbox();

	//Session sess = Sessions.getCurrent();

	String source = "";

	String titulo = "";

	@Init
	public void init(@ContextParam(ContextType.COMPONENT) Component comp) {

		log.info("Init DeviceVM");
		devicesCheck = new ArrayList<Addins>();
		// se recupera el dato del textbox que es el que nos indica a que descriptor
		// vamos
		data = (Textbox) comp.getNextSibling();
		if (data != null) {
			source = data.getValue();
			titulo = Labels.getLabel("title.header." + source);
		}
		// load instructor list from the database
		loadDevices();

		// initialize filter to null (no value)
		setFilter(null);
	}

	/**
	 * Get the Devices list from the database
	 *
	 * @return A list of Devices elements
	 * @see Addins
	 */
	public List<Addins> getDevicesListFromDatabase() {

		return getFilter() != null
				? addinsMag.getFiltered(getFilter()).stream().filter(f -> f.getAddinsDev().getDevgroup().equals(source))
						.collect(Collectors.toList())
				: addinsMag.getAll().stream().filter(f -> f.getAddinsDev() != null)
						.filter(f -> f.getAddinsDev().getDevgroup().equals(source)).collect(Collectors.toList());
	}

	/**
	 * Reload the usuario list from the database
	 *
	 * @see Instructor
	 */
	@GlobalCommand
	@NotifyChange("devices")
	public void loadDevices() {
		log.info("source-> " + source);
		this.devices = getDevicesListFromDatabase();
	}

	@Command
	@NotifyChange("devices")
	public void removeDevice(@BindingParam("device") Addins addins) {

		deleteRegistro(addins);

	}

	private void deleteRegistro(Addins addins) {
		try {
			// confirmation dialog

			EventListener<Messagebox.ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(Messagebox.ClickEvent event) throws Exception {
					if (Messagebox.Button.YES.equals(event.getButton())) {

						// store ids of the new edited list
						if (addins != null) {

							addinsMag.delete(addins);

							// el envio del xml pa luego
							// resourceXml.deleteMsg(addins);
							BindUtils.postGlobalCommand(null, null, "loadDevices", null);

							// show notification
							ApplicationUtils.showInfo("message.registroEliminado");
						} else {

							for (Addins addins : devicesCheck) {
								addinsMag.delete(addins);

							}
							BindUtils.postGlobalCommand(null, null, "loadDevices", null);

						}
					}
				}
			};
			Messagebox.show(Labels.getLabel("message.deleteConfirmation"), "Confirmation",
					new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION,
					clickListener);

		} catch (Exception e) {
			log.error("Error when saving user list : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("devices")
	public void showAdd(@BindingParam("addins") Addins addins) {

		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("source", source);
		if (devices != null)

			map.put("addins", addins);

		wDevices = (Window) Executions.createComponents("~./zul/createdevice.zul", null, map);

		wDevices.doModal();
	}

	@Command
	@NotifyChange("devices")
	public void showEditMulti() {

		showEdit(null);

	}

	@Command
	@NotifyChange("devices")
	public void showEdit(@BindingParam("device") Addins addins) {

		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("source", source);

		map.put("addins", addins);
		
		map.put("addinsList", devicesCheck);

		wDevices = (Window) Executions.createComponents("~./zul/editardevice.zul", null, map);

		wDevices.doModal();
	}

	@Command
	public void addCheckMulti(@BindingParam("device") Addins addins) {

		if (devicesCheck.contains(addins))
			devicesCheck.remove(addins);
		else
			devicesCheck.add(addins);
	}

	@Command
	public void onSelectAll(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent e) {

		System.out.println(e.getName());

		if (e.isChecked())
			devicesCheck.addAll(devices);
		else
			devicesCheck.clear();
	}

	@Command
	public void removeMulti() {

		System.out.println(devicesCheck.size());
		// devicesCheck.stream().forEach(j->System.out.println(j.getId()));
		deleteRegistro(null);
	}

}
