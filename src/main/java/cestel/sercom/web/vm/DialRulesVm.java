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

import cestel.sercom.web.entity.DialRules;
import cestel.sercom.web.service.DialRulesManager;
import cestel.sercom.web.util.ApplicationUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DialRulesVm {

	private List<DialRules> dialRules = new ArrayList<DialRules>();

	private List<DialRules> dialRulesCheck;

	// initialize filter to null (no value)
	private String filter;


	
	@WireVariable
	private DialRulesManager dialRuleMag;

	@Init
	public void init() {

		log.info("Init DialRulesVM");

		dialRulesCheck = new ArrayList<DialRules>();
		loadDialRules();

		// initialize filter to null (no value)
		setFilter(null);
	}

	/**
	 * Get the instructor list from the database
	 *
	 * @return A list of Instructor elements representing the instructors
	 * @see Instructor
	 */
	public List<DialRules> getDialRulesListFromDatabase() {
		return getFilter() != null ? dialRuleMag.getFiltered(getFilter()) : dialRuleMag.getAllDialRules();
	}

	/**
	 * Reload the usuario list from the database
	 *
	 * @see Instructor
	 */
	@GlobalCommand
	@NotifyChange("dialRules")
	public void loadDialRules() {
		this.dialRules = getDialRulesListFromDatabase();
	}

	@GlobalCommand
	@NotifyChange("dialRulesCheck")
	public void loadDialRulesCheck() {
		dialRulesCheck.clear();
	}

	@Command
	@NotifyChange("dialRules")
	public void removeDialRules(@BindingParam("dialRules") DialRules dialRules) {

		deleteRegistro(dialRules);

	}
	private void deleteRegistro(DialRules dialRules) {
		try {
			// confirmation dialog
		
			EventListener<Messagebox.ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(Messagebox.ClickEvent event) throws Exception {
					if (Messagebox.Button.YES.equals(event.getButton())) {

						// store ids of the new edited list
						if(dialRules!=null) {
							
							dialRuleMag.delete(dialRules);

						// el envio del xml pa luego
						// resourceXml.deleteMsg(addins);
						BindUtils.postGlobalCommand(null, null, "loadDialRules", null);

						// show notification
						ApplicationUtils.showInfo("message.registroEliminado");
						}else {
							
							for(DialRules dialRules:dialRulesCheck) {
								dialRuleMag.delete(dialRules);
								
							}
							BindUtils.postGlobalCommand(null, null, "loadDialRules", null);
							
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
	@NotifyChange("dialRules")
	public void showEdit(@BindingParam("dialRules") DialRules dialRules) {
		Window window;
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (dialRules != null) {

			map.put("dialRules", dialRules);
			// si va con el dns es mara modificar (con el map)
			window = (Window) Executions.createComponents("~./zul/editardialRules.zul", null, map);
		} else if (dialRulesCheck != null && dialRulesCheck.size() > 0) {
			map.put("dialRulesCheck", dialRulesCheck);
			window = (Window) Executions.createComponents("~./zul/editardialRules.zul", null, map);

		} else {
			window = (Window) Executions.createComponents("~./zul/editardialRules.zul", null, null);
		}
		window.doModal();

	}

	@Command
	@NotifyChange("dialRules")
	public void showEditMulti() {

		showEdit(null);

	}

	@Command
	public void addCheckMulti(@BindingParam("dialRules") DialRules dialRules) {

		if (dialRulesCheck.contains(dialRules))
			dialRulesCheck.remove(dialRules);
		else
			dialRulesCheck.add(dialRules);
	}

	@Command
	public void onSelectAll(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent e) {

		if (e.isChecked())
			dialRulesCheck.addAll(dialRules);

	}
	@Command
	public void removeMulti() {
		deleteRegistro(null);
	}

}