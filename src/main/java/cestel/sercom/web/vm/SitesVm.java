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

import cestel.sercom.web.entity.Sites;
import cestel.sercom.web.service.SitesManager;
import cestel.sercom.web.util.ApplicationUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SitesVm {

	private List<Sites> sites = new ArrayList<Sites>();

	private List<Sites> sitesCheck;

	// initialize filter to null (no value)
	private String filter;


	
	@WireVariable
	private SitesManager siteMag;

	@Init
	public void init() {

		log.info("Init SitesVM");

		sitesCheck = new ArrayList<Sites>();
		loadSites();

		// initialize filter to null (no value)
		setFilter(null);
	}

	/**
	 * Get the instructor list from the database
	 *
	 * @return A list of Instructor elements representing the instructors
	 * @see Instructor
	 */
	public List<Sites> getSitesListFromDatabase() {
		return getFilter() != null ? siteMag.getFiltered(getFilter()) : siteMag.getAllSites();
	}

	/**
	 * Reload the usuario list from the database
	 *
	 * @see Instructor
	 */
	@GlobalCommand
	@NotifyChange("sites")
	public void loadSites() {
		this.sites = getSitesListFromDatabase();
	}

	@GlobalCommand
	@NotifyChange("sitesCheck")
	public void loadSitesCheck() {
		sitesCheck.clear();
	}

	@Command
	@NotifyChange("sites")
	public void removeSites(@BindingParam("sites") Sites sites) {

		deleteRegistro(sites);

	}
	private void deleteRegistro(Sites sites) {
		try {
			// confirmation dialog
		
			EventListener<Messagebox.ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(Messagebox.ClickEvent event) throws Exception {
					if (Messagebox.Button.YES.equals(event.getButton())) {

						// store ids of the new edited list
						if(sites!=null) {
							
							siteMag.delete(sites);

						// el envio del xml pa luego
						// resourceXml.deleteMsg(addins);
						BindUtils.postGlobalCommand(null, null, "loadSites", null);

						// show notification
						ApplicationUtils.showInfo("message.registroEliminado");
						}else {
							
							for(Sites sites:sitesCheck) {
								siteMag.delete(sites);
								
							}
							BindUtils.postGlobalCommand(null, null, "loadSites", null);
							
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
	@NotifyChange("sites")
	public void showEdit(@BindingParam("sites") Sites sites) {
		Window window;
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (sites != null) {

			map.put("sites", sites);
			// si va con el dns es mara modificar (con el map)
			window = (Window) Executions.createComponents("~./zul/editarsites.zul", null, map);
		} else if (sitesCheck != null && sitesCheck.size() > 0) {
			map.put("sitesCheck", sitesCheck);
			window = (Window) Executions.createComponents("~./zul/editarsites.zul", null, map);

		} else {
			window = (Window) Executions.createComponents("~./zul/editarsites.zul", null, null);
		}
		window.doModal();

	}

	@Command
	@NotifyChange("sites")
	public void showEditMulti() {

		showEdit(null);

	}

	@Command
	public void addCheckMulti(@BindingParam("sites") Sites sites) {

		if (sitesCheck.contains(sites))
			sitesCheck.remove(sites);
		else
			sitesCheck.add(sites);
	}

	@Command
	public void onSelectAll(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent e) {

		if (e.isChecked())
			sitesCheck.addAll(sites);

	}
	@Command
	public void removeMulti() {
		deleteRegistro(null);
	}

}