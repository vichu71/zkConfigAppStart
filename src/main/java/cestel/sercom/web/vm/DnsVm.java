package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cestel.sercom.web.entity.Dns;
import cestel.sercom.web.service.DnsManager;
import cestel.sercom.web.util.ApplicationUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DnsVm {

	private List<Dns> dns = new ArrayList<Dns>();

	private List<Dns> dnsCheck;

	// initialize filter to null (no value)
	private String filter;


	@WireVariable
	private DnsManager dnsMag;

	@Init
	public void init() {

		log.info("Init UsuarioVM");

		dnsCheck = new ArrayList<Dns>();
		loadDns();

		// initialize filter to null (no value)
		setFilter(null);
	}

	/**
	 * Get the instructor list from the database
	 *
	 * @return A list of Instructor elements representing the instructors
	 * @see Instructor
	 */
	public List<Dns> getDnsListFromDatabase() {
		return getFilter() != null ? dnsMag.getFiltered(getFilter()) : dnsMag.getAll();
	}

	/**
	 * Reload the usuario list from the database
	 *
	 * @see Instructor
	 */
	@GlobalCommand
	@NotifyChange("dns")
	public void loadDns() {
		this.dns = getDnsListFromDatabase();
	}

	@GlobalCommand
	@NotifyChange("dnsCheck")
	public void loadDnsCheck() {
		dnsCheck.clear();
	}

	@Command
	@NotifyChange("dns")
	public void removeDns(@BindingParam("dns") Dns dns) {

		try {
			// confirmation dialog
			EventListener<Messagebox.ClickEvent> clickListener = new EventListener<>() {
				public void onEvent(Messagebox.ClickEvent event) throws Exception {
					if (Messagebox.Button.YES.equals(event.getButton())) {

						// store ids of the new edited list

						dnsMag.delete(dns);

						BindUtils.postGlobalCommand(null, null, "loadDns", null);

						// show notification
						ApplicationUtils.showInfo("message.registroEliminado");

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
	@NotifyChange("dns")
	public void showEdit(@BindingParam("dns") Dns dns) {
		Window window;
		// UIUtils.show("~./zul/appconfig/editarusuario.zul", null, usuario);
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (dns != null) {

			map.put("dns", dns);
			// si va con el dns es mara modificar (con el map)
			window = (Window) Executions.createComponents("~./zul/appconfig/editardns.zul", null, map);
		} else if (dnsCheck != null && dnsCheck.size() > 0) {
			map.put("dnsCheck", dnsCheck);
			window = (Window) Executions.createComponents("~./zul/appconfig/editardns.zul", null, map);

		} else {
			window = (Window) Executions.createComponents("~./zul/appconfig/editardns.zul", null, null);
		}
		window.doModal();

	}

	@Command
	@NotifyChange("dns")
	public void showEditMulti() {

		showEdit(null);

	}

	@Command
	public void addCheckMulti(@BindingParam("dns") Dns dns) {

		if (dnsCheck.contains(dns))
			dnsCheck.remove(dns);
		else
			dnsCheck.add(dns);
	}

	@Command
	public void onSelectAll(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent e) {

		if (e.isChecked())
			dnsCheck.addAll(dns);

	}

}