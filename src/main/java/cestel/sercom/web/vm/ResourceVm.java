package cestel.sercom.web.vm;

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
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cestel.sercom.web.entity.Resource;
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
public class ResourceVm {

	private List<Resource> resources;

	// initialize filter to null (no value)
	private String filter;

	@WireVariable
	private ResourceManager resourceMag;
	@WireVariable
	private ResourceXMLImpl resourceXml;

	// Componentes UI
	// @Wire
	Grid grid1;

	// @Wire
	Window wResource;

// @Wire  
	Textbox data = new Textbox();

	Session sess = Sessions.getCurrent();

	String source = "";
	
	String titulo="";

	

	@Init
	public void init(@ContextParam(ContextType.COMPONENT) Component comp) {

		log.info("Init ResourceVM");
		//se recupera el dato del textbox que es el que nos indica a que descriptor vamos
		data = (Textbox) comp.getNextSibling();
		if (data != null) {
			source = data.getValue();
			titulo= Labels.getLabel("title.header."+source);
		}
		// load instructor list from the database
		loadResources();

		// initialize filter to null (no value)
		setFilter(null);
	}

	/**
	 * Get the instructor list from the database
	 *
	 * @return A list of Instructor elements representing the instructors
	 * @see Instructor
	 */
	public List<Resource> getResourcesListFromDatabase() {
		return getFilter() != null
				? resourceMag.getFiltered(getFilter()).stream().filter(f -> f.getResclass().equals(source))
						.collect(Collectors.toList())
				: resourceMag.getAll().stream().filter(f -> f.getResclass().equals(source))
						.collect(Collectors.toList());
	}

	/**
	 * Reload the usuario list from the database
	 *
	 * @see Instructor
	 */
	@GlobalCommand
	@NotifyChange("resources")
	public void loadResources() {
		System.out.println("source-> " + source);
		this.resources = getResourcesListFromDatabase();
	}

	@Command
	@NotifyChange("resources")
	public void removeResource(@BindingParam("resource") Resource resource) {

		try {
			// confirmation dialog
			EventListener<Messagebox.ClickEvent> clickListener = new EventListener<>() {
				public void onEvent(Messagebox.ClickEvent event) throws Exception {
					if (Messagebox.Button.YES.equals(event.getButton())) {

						// store ids of the new edited list

						resourceMag.delete(resource);
						
					resourceXml.deleteMsg(resource);
						BindUtils.postGlobalCommand(null, null, "loadResources", null);

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
	@NotifyChange("resources")
	public void showEdit(@BindingParam("resource") Resource resource) {
		// Window wResource;
//		// UIUtils.show("~./zul/appconfig/editarusuario.zul", null, usuario);
		final HashMap<String, Object> map = new HashMap<String, Object>();
		if (resources != null) {
			

			map.put("resource", resource);
			map.put("source", source);
			wResource = (Window) Executions.createComponents("/zul/editarresource.zul", null, map);
		} else {
			map.put("source", source);
			wResource = (Window) Executions.createComponents("/zul/editarresource.zul", null, map);

		}
		wResource.doModal();
	}

}
