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
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cestel.sercom.web.entity.Dominio;
import cestel.sercom.web.entity.SubDominio;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.service.DominioManager;
import cestel.sercom.web.service.SubDominioManager;
import cestel.sercom.web.util.ApplicationUtils;
import cestel.sercom.web.util.ComboDto;
import cestel.sercom.web.vm.bean.SubDominioVmBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SubDominioVm {

	//private List<Dominio> dominioList = new ArrayList<Dominio>();
	private List<SubDominio> subDominioList = new ArrayList<SubDominio>();

	private List<SubDominio> 	subDominioCheck;

	// initialize filter to null (no value)
	private String filter;

	private SubDominioVmBean subDominioVmBean;
	
	
	
	@WireVariable
	private SubDominioManager subDomMag;

	@Init
	public void init() {

		log.info("Init SubDominioVM");
		
		User userLoginSession = null;
		Session session = Sessions.getCurrent();
		userLoginSession = (User) session.getAttribute("connectedUser");

		subDominioCheck = new ArrayList<SubDominio>();
		subDominioVmBean = new SubDominioVmBean();
		subDominioList =	subDomMag.getSubDominioByDominio(userLoginSession.getDomid());
	

		// initialize filter to null (no value)
		setFilter(null);
	}
	

	
	public List<SubDominio> getSubDominioListFromDatabase() {
		return getFilter() != null ? subDomMag.getFiltered(getFilter()) : subDomMag.getAllSubDominio();
	}

	/**
	 * Reload the  list from the database
	 *
	 *
	 */
	@GlobalCommand
	@NotifyChange("subDominioList")
	public void loadSubDominios() {
		this.subDominioList = getSubDominioListFromDatabase();
	}

	@GlobalCommand
	@NotifyChange("subDominioCheck")
	public void loadSubDominioCheck() {
		subDominioCheck.clear();
	}

	@Command
	@NotifyChange("subDominioList")
	public void removeSubDominio(@BindingParam("subDominioList") SubDominio subDominio) {

		deleteRegistro(subDominio);

	}
	private void deleteRegistro(SubDominio subDominio) {
		try {
			// confirmation dialog
		
			EventListener<Messagebox.ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(Messagebox.ClickEvent event) throws Exception {
					if (Messagebox.Button.YES.equals(event.getButton())) {

						// store ids of the new edited list
						if(subDominio!=null) {
							
							subDomMag.delete(subDominio);

						// el envio del xml pa luego
						// resourceXml.deleteMsg(addins);
						BindUtils.postGlobalCommand(null, null, "loadSubDominios", null);

						// show notification
						ApplicationUtils.showInfo("message.registroEliminado");
						}else {
							
							for(SubDominio subDominio:subDominioCheck) {
								subDomMag.delete(subDominio);
								
							}
							BindUtils.postGlobalCommand(null, null, "loadSubDominios", null);
							
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
	@NotifyChange("subDominioList")
	public void showEdit(@BindingParam("subDominioList") SubDominio subDominio) {
		Window window;
		
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (subDominio != null) {

			map.put("subDominio", subDominio);
			// si va con el dns es mara modificar (con el map)
			window = (Window) Executions.createComponents("~./zul/editarsubdominio.zul", null, map);
		} else if (subDominioCheck != null && subDominioCheck.size() > 0) {
			map.put("subDominioCheck", subDominioCheck);
			window = (Window) Executions.createComponents("~./zul/editarsubdominio.zul", null, map);

		} else {
			window = (Window) Executions.createComponents("~./zul/editarsubdominio.zul", null, null);
		}
		window.doModal();

	}

	@Command
	@NotifyChange("subDominioList")
	public void showEditMulti() {

		showEdit(null);

	}

	@Command
	public void addCheckMulti(@BindingParam("subDominioList") SubDominio subDominio) {

		if (subDominioCheck.contains(subDominio))
			subDominioCheck.remove(subDominio);
		else
			subDominioCheck.add(subDominio);
	}

	@Command
	public void onSelectAll(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent e) {

		if (e.isChecked())
			subDominioCheck.addAll(subDominioList);

	}
	@Command
	public void removeMulti() {
		deleteRegistro(null);
	}

}