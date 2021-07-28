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

import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.service.DominioManager;
import cestel.sercom.web.util.ApplicationUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DominioVm {

	private List<Dominio> dominio = new ArrayList<Dominio>();

	private List<Dominio> dominioCheck;

	// initialize filter to null (no value)
	private String filter;


	
	@WireVariable
	private DominioManager domMag;

	@Init
	public void init() {

		log.info("Init DominioVM");

		dominioCheck = new ArrayList<Dominio>();
		loadDominio();

		// initialize filter to null (no value)
		setFilter(null);
	}

	/**
	 * Get the instructor list from the database
	 *
	 * @return A list of Instructor elements representing the instructors
	 * @see Instructor
	 */
	public List<Dominio> getDominioListFromDatabase() {
		return getFilter() != null ? domMag.getFiltered(getFilter()) : domMag.getAllDominio();
	}

	/**
	 * Reload the usuario list from the database
	 *
	 * @see Instructor
	 */
	@GlobalCommand
	@NotifyChange("dominio")
	public void loadDominio() {
		this.dominio = getDominioListFromDatabase();
	}

	@GlobalCommand
	@NotifyChange("dominioCheck")
	public void loadDominioCheck() {
		dominioCheck.clear();
	}

	@Command
	@NotifyChange("dominio")
	public void removeDns(@BindingParam("dominio") Dominio dominio) {

		deleteRegistro(dominio);

	}
	private void deleteRegistro(Dominio dominio) {
		try {
			// confirmation dialog
		
			EventListener<Messagebox.ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(Messagebox.ClickEvent event) throws Exception {
					if (Messagebox.Button.YES.equals(event.getButton())) {

						// store ids of the new edited list
						if(dominio!=null) {
							
							domMag.delete(dominio);

						// el envio del xml pa luego
						// resourceXml.deleteMsg(addins);
						BindUtils.postGlobalCommand(null, null, "loadDominio", null);

						// show notification
						ApplicationUtils.showInfo("message.registroEliminado");
						}else {
							
							for(Dominio dominio:dominioCheck) {
								domMag.delete(dominio);
								
							}
							BindUtils.postGlobalCommand(null, null, "loadDominio", null);
							
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
	@NotifyChange("dominio")
	public void showEdit(@BindingParam("dominio") Dominio dominio) {
		Window window;
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (dominio != null) {

			map.put("dominio", dominio);
			// si va con el dns es mara modificar (con el map)
			window = (Window) Executions.createComponents("~./zul/editardominio.zul", null, map);
		} else if (dominioCheck != null && dominioCheck.size() > 0) {
			map.put("dominioCheck", dominioCheck);
			window = (Window) Executions.createComponents("~./zul/editardominio.zul", null, map);

		} else {
			window = (Window) Executions.createComponents("~./zul/editardominio.zul", null, null);
		}
		window.doModal();

	}

	@Command
	@NotifyChange("dominio")
	public void showEditMulti() {

		showEdit(null);

	}

	@Command
	public void addCheckMulti(@BindingParam("dominio") Dominio dominio) {

		if (dominioCheck.contains(dominio))
			dominioCheck.remove(dominio);
		else
			dominioCheck.add(dominio);
	}

	@Command
	public void onSelectAll(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent e) {

		if (e.isChecked())
			dominioCheck.addAll(dominio);

	}
	@Command
	public void removeMulti() {
		deleteRegistro(null);
	}

}