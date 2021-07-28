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
public class PluginsVm {

	private List<Addins> plugins;

	private List<Addins> pluginsCheck;

	// initialize filter to null (no value)
	private String filter;

	@WireVariable
	private AddinsManager addinsMag;
	@WireVariable
	private ResourceXMLImpl resourceXml;

	Grid grid1;
	Window wPlugins;
	Textbox data = new Textbox();

	//Session sess = Sessions.getCurrent();

	String source = "";

	String titulo = "";

	@Init
	public void init(@ContextParam(ContextType.COMPONENT) Component comp) {

		log.info("Init PluginsVM");
		pluginsCheck = new ArrayList<Addins>();
		// se recupera el dato del textbox que es el que nos indica a que descriptor
		// vamos
		data = (Textbox) comp.getNextSibling();
		if (data != null) {
			source = data.getValue();
			titulo = Labels.getLabel("title.header." + source);
		}
		// load instructor list from the database
		loadPlugin();

		// initialize filter to null (no value)
		setFilter(null);
	}

	/**
	 * Get the plugins list from the database
	 *
	 * @return A list of Plugins elements
	 * @see Addins
	 */
	public List<Addins> getPluginsListFromDatabase() {

		return getFilter() != null
				? addinsMag.getFiltered(getFilter()).stream().filter(f -> f.getAddinsPlg().getPclass().equals(source))
						.collect(Collectors.toList())
				: addinsMag.getAll().stream().filter(f -> f.getAddinsPlg() != null)
						.filter(f -> f.getAddinsPlg().getPclass().equals(source)).collect(Collectors.toList());
	}

	/**
	 * Reload the usuario list from the database
	 *
	 * @see Instructor
	 */
	@GlobalCommand
	@NotifyChange("plugins")
	public void loadPlugin() {
		log.info("source-> " + source);
		this.plugins = getPluginsListFromDatabase();
		System.out.println(plugins);
	}

	@Command
	@NotifyChange("plugins")
	public void removePlugin(@BindingParam("plugin") Addins addins) {

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
							BindUtils.postGlobalCommand(null, null, "loadPlugin", null);

							// show notification
							ApplicationUtils.showInfo("message.registroEliminado");
						} else {

							for (Addins addins : pluginsCheck) {
								addinsMag.delete(addins);

							}
							BindUtils.postGlobalCommand(null, null, "loadPlugin", null);

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
	@NotifyChange("plugins")
	public void showAdd(@BindingParam("addins") Addins addins) {

		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("source", source);
		if (plugins != null)

			map.put("addins", addins);

		wPlugins = (Window) Executions.createComponents("~./zul/createplugin.zul", null, map);

		wPlugins.doModal();
	}

	@Command
	@NotifyChange("plugins")
	public void showEditMulti() {

		showEdit(null);

	}

	@Command
	@NotifyChange("plugins")
	public void showEdit(@BindingParam("plugin") Addins addins) {

		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("source", source);

		map.put("addins", addins);
		
		map.put("addinsList", pluginsCheck);

		wPlugins = (Window) Executions.createComponents("~./zul/editarplugin.zul", null, map);

		wPlugins.doModal();
	}

	@Command
	public void addCheckMulti(@BindingParam("plugin") Addins addins) {

		if (pluginsCheck.contains(addins))
			pluginsCheck.remove(addins);
		else
			pluginsCheck.add(addins);
	}

	@Command
	public void onSelectAll(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent e) {

		System.out.println(e.getName());

		if (e.isChecked())
			pluginsCheck.addAll(plugins);
		else
			pluginsCheck.clear();
	}

	@Command
	public void removeMulti() {

		System.out.println(pluginsCheck.size());
		
		deleteRegistro(null);
	}

}
