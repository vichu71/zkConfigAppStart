package cestel.sercom.web.vm;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cestel.sercom.web.entity.Dns;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.service.DnsManager;
import cestel.sercom.web.service.UserManager;
import cestel.sercom.web.util.ApplicationUtils;
import cestel.sercom.web.util.UIUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DnsVm {

	
	private List<Dns> dns;

	// initialize filter to null (no value)
	private String filter;

//	@WireVariable
//	private UserManager userMag;
	
	@WireVariable
	private DnsManager dnsMag;
	
	@Init
	public void init() {
	
		log.info("Init UsuarioVM");

		// load instructor list from the database
		loadUsuarios();

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
	public void loadUsuarios() {
		this.dns = getDnsListFromDatabase();
	}


	@Command
	@NotifyChange("dns")
	public void removeUsuario(@BindingParam("dns") Dns dns) {
		
		try {
			// confirmation dialog
			EventListener<Messagebox.ClickEvent> clickListener = new EventListener<>() {
				public void onEvent(Messagebox.ClickEvent event) throws Exception {
					if (Messagebox.Button.YES.equals(event.getButton())) {

						// store ids of the new edited list

						dnsMag.delete(dns);
						
						BindUtils.postGlobalCommand(null, null, "loadDns", null);

						// show notification
						ApplicationUtils.showInfo("message.DeviceEliminado");
						
						
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

	/**
	 * Save the current instructor list to the database
	 *
	 * @see Instructor
	 */
//    @Command
//    @NotifyChange("usuarios")
//    public void saveUsuarios() {
//        try {
//            // confirmation dialog
//            EventListener<Messagebox.ClickEvent> clickListener = new EventListener<>() {
//                public void onEvent(Messagebox.ClickEvent event) throws Exception {
//                    if (Messagebox.Button.YES.equals(event.getButton())) {
//
//                        // store ids of the new edited list
//                        final List<Long> usuarioIds = new ArrayList<>();
//                        for (User is : getUsuarios()) {
//                        	usuarioIds.add(is.getUsuario().getId());
//                        }
//
//                        // loop through all database values and remove the ones that or not in the new list
//                        for (User usu : userMag.getAll()) {
//                            if (!usuarioIds.contains(usu.getId())) {
//                                // if instructor is still used somewhere
//                                //if (userMag.isUsed(i.getId()).isPresent()) {
//                                    // show notification
//                                  //  ApplicationUtils.showError("message.UsuarioStillUsed", i.getName());
//                              //  } else {
//                                	userMag.delete(usu);
//                              //  }
//                            }
//                        }
//
//                        // update the database with the new values
//                        for (UsuarioStatus is : getUsuarios()) {
//                        	userMag.saveOrUpdate(is.getUsuario());
//                        }
//
//                        // show notification
//                        ApplicationUtils.showInfo("message.instructorsSaved");
//                    }
//                }
//            };
//            Messagebox.show(Labels.getLabel("message.saveModificationsConfirmation"), "Confirmation", new Messagebox.Button[]{Messagebox.Button.YES, Messagebox.Button.NO}, Messagebox.QUESTION, clickListener);
//        } catch (Exception e) {
//           // logger.error("Error when saving instructor list : " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

	
	@Command
	@NotifyChange("dns")
	public void showEdit(@BindingParam("dns") Dns dns) {
		 Window window;
		//UIUtils.show("~./zul/appconfig/editarusuario.zul", null, usuario);
		if(dns!=null) {
		final HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("dns", dns);
        //si va con el usuario es mara modificar (con el map)
        window = (Window) Executions.createComponents("~./zul/appconfig/editarusuario.zul", null, map);
		}else {
			 window = (Window) Executions.createComponents("~./zul/appconfig/editarusuario.zul", null, null);	
			
		}
        window.doModal();
	}

}