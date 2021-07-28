package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.xml.sax.SAXException;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.DropEvent;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cestel.sercom.web.descriptor.bean.ResClassPropBean;
import cestel.sercom.web.descriptor.bean.ResOptionsBean;
import cestel.sercom.web.entity.Addins;
import cestel.sercom.web.entity.AddinsDev;
import cestel.sercom.web.entity.AddinsPlg;
import cestel.sercom.web.entity.AddinsProp;
import cestel.sercom.web.entity.PropResource;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.entity.User;
import cestel.sercom.web.exception.CxException;
import cestel.sercom.web.service.AddinsDevManager;
import cestel.sercom.web.service.ConexionSercomManager;
import cestel.sercom.web.service.DescriptorManager;
import cestel.sercom.web.service.PropAddinsManager;
import cestel.sercom.web.service.PropResourceManager;
import cestel.sercom.web.service.ResourceManager;
import cestel.sercom.web.service.imp.ResourceXMLImpl;
import cestel.sercom.web.vm.bean.ResourceVmBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditPluginVm extends SelectorComposer<Component> {

	@WireVariable
	private ResourceManager resourceMag;

	@WireVariable
	private PropResourceManager propresourceMag;

	@WireVariable
	private DescriptorManager decriptorMag;

	@WireVariable
	private ResourceXMLImpl resourceXml;

	@WireVariable
	private ConexionSercomManager conserMag;
	
	@WireVariable
	private AddinsDevManager addinsDevMag;
	
	@WireVariable
	private PropAddinsManager propaddinsMag;
	

	private List<ResClassPropBean> resClassProp = new ArrayList<>();;

	List<String> optResource = new ArrayList<>();

	// private String nombre;
	
	@Wire
	private Label type;
	@Wire
	private Label version;
	@Wire
	private Label family;
	

	private String skill;

	// private List<String> listSkill = new ArrayList<String>();

	private ListModelList<String> listSkill = new ListModelList<String>();

	Map<String, String> valueLabelmap;

	private ResourceVmBean resourceVmBean;

	String propiedad = "";
	AtomicReference<String> propiedadAtto = new AtomicReference<>();

	String atributosCelList = "";

	Map<String, String> prop = new HashMap<>();
	Map<String, String> propresou = new HashMap<>();

	@Wire
	private Div divcontainer;

	@Wire
	private Label titulo;

	Textbox textbox1;

	Textbox textbox2;

	Columns columnas;
	Column columna1;
	Column columna2;
	Column columna3;
	Column columna4;
	Column columna5;
	Column columna6;
	Column columna7;
	Rows rows;
	Row row;
	Row row2;

	Label label1;
	Label label2;

	private Grid grid1;
	private Grid grid2;
	private Grid grid3;

	Combobox combobox;
	Comboitem comboitem;

	Radiogroup radiogroup;
	Radio radiotrue;
	Radio radiofalse;

	private String source;
	
	private Addins addins;

	PropResource propresource;
	User userLoginSession = null;
	Session session = Sessions.getCurrent();
	AddinsProp addinsProp;

	List<AddinsProp> listaPropiedadesPorIdPlugin = new ArrayList<>();
	Component comp;

	private List<Addins> addinsList;

	@Override
	public void doAfterCompose(Component comp) throws Exception {

		this.comp = comp;
		super.doAfterCompose(comp);
		userLoginSession = (User) session.getAttribute("connectedUser");
		final Execution execution = Executions.getCurrent();

		// estos son los atributos que vienen desde la creaccion de la ventana
		this.source = (String) execution.getArg().get("source");
		this.addins = (Addins) execution.getArg().get("addins");
		this.addinsList = (List<Addins>) execution.getArg().get("addinsList");
		
		addLabel(titulo, Labels.getLabel("title.header." + source), "");
		resourceVmBean = new ResourceVmBean();

		modificarResource();
		
		}

	private void crearResource() throws SAXException {
		// resClassProp = decriptorMag.cargaDescriptorResources("ResourceDesc_" +
		// source);
		// createND();
		resClassProp.stream().forEach(prop -> {

			log.info(prop.getType());
			resourceVmBean.setName(prop.getName());
			resourceVmBean.setInfo(prop.getDescripcion());
			resourceVmBean.setType(prop.getType());

			if ("STRING_LIST".equals(prop.getType())) {
				Optional<String> cadenaopt = Optional.empty();
				String cadena = "";

				
					cadenaopt = listaPropiedadesPorIdPlugin.stream()
							.filter(fil -> fil.getName().equals(prop.getName())).map(AddinsProp::getValue).findAny();
					if (cadenaopt.isPresent()) {

						cadena = cadenaopt.get();
					}else {
						
						cadena = prop.getDefval();
					}
				
				String[] parts = cadena.split(";");

				listSkill = new ListModelList<String>(parts);

				createSL();
			} else if ("STRING".equals(prop.getType())) {

				createS();
			}  else if ("SITE".equals(prop.getType())) {

				createSITE();
			} else if ("OPTION".equals(prop.getType())) {

				resourceVmBean.setSelecteditem(prop.getDefval());
				valueLabelmap = prop.getResOptions().stream()
						.collect(Collectors.toMap(ResOptionsBean::getValue, ResOptionsBean::getLabel));

				createOP();
			} else if ("BOOLEAN".equals(prop.getType())) {

				resourceVmBean.setSelecteditem(prop.getDefval());
				createBOO();
			} else if ("INTEGER".equals(prop.getType())) {

				resourceVmBean.setSelecteditem(prop.getDefval());
				createS();
			} else if ("RESOURCE".equals(prop.getType())) {

				resourceVmBean.setSelecteditem(prop.getDefval());
				resourceVmBean.setClaseresource(prop.getResClassCode());
				valueLabelmap = new HashMap<>();
//				getResourceByTypo(prop.getResClassCode());
//				valueLabelmap.put("-none-", "-none-");

				valueLabelmap = getResourceByTypo(prop.getResClassCode()).stream()
						.collect(Collectors.toMap(Resource::getIdString, Resource::getName));
				valueLabelmap.put("-none-", "-none-");
				createOP();
			}

		});
	}

	public List<Resource> getResourceByTypo(String classe) {
		List<Resource> listResources = resourceMag.getResourcebyClase(classe);
		return listResources;

		// cmbTeam.setSelectedIndex(teamname.size()-1);
	}

	private void modificarResource() throws SAXException {
		
		
		log.info("modifico-> " + addins.getAddinsPlg().getPclass());
		log.info("modifico-> " + addins.getAddinsPlg().getId());
		//name.setValue(addins.getAddinsDev().getName());
		type.setValue(addins.getType());
		version.setValue(addins.getVersion());
		family.setValue(addins.getFamily());
	

		listaPropiedadesPorIdPlugin = addins.getAddinsPropList();

		// resClassProp = decriptorMag.cargaDescriptorResources("ResourceDesc_" +
		// source);

		resClassProp = decriptorMag.cargaPropDescriptorDeviceMap(source, addins.getFamily(),
				addins.getType() + "_" + addins.getVersion() + ".xml");
		log.info("listaPropiedadesPorIdResource-> " + resClassProp.size());
	//	plugins.setValue( decriptorMag.cargaplugins().getDescripcionAndPlugind().getPlugins());
		crearResource();

	}

	private void createBOO() {
		plantillaColumnas();
		addLabel(label1, resourceVmBean.getName(), "");
		createComponent();

		createRadiogroup(resourceVmBean.getName());

		row.appendChild(radiogroup);
	}
	private void createSITE() {
		plantillaColumnas();

		textbox1 = new Textbox();
		addLabel(label1, resourceVmBean.getName(), "");
		// textbox1.setName(resourceVmBean.getName());
		textbox1.setAttribute("nombreatt", resourceVmBean.getName());
//
//		if (!modificacion) {
//			textbox1.setValue("");
//		} else {

			Optional<String> propiedad = listaPropiedadesPorIdPlugin.stream()
					.filter(fil -> fil.getName().equals(resourceVmBean.getName())).map(AddinsProp::getValue)
					.findAny();

			if (propiedad.isPresent())
				textbox1.setValue(propiedad.get());
			else
				textbox1.setValue("");	
//		}

		createComponent();
		row.appendChild(textbox1);
	}

	private void createRadiogroup(String name) {

		radiogroup = new Radiogroup();

		radiogroup.setAttribute("nombreatt", name);
		radiotrue = new Radio();
		radiofalse = new Radio();
		radiotrue.setLabel("True");
		radiofalse.setLabel("False");

		radiogroup.appendChild(radiotrue);
		radiogroup.appendChild(radiofalse);

//		if (!modificacion) {
//			if ("true".equals(resourceVmBean.getSelecteditem()))
//				radiogroup.setSelectedItem(radiotrue);
//			else
//				radiogroup.setSelectedItem(radiofalse);
//
//		} else
//
//		{
			Optional<String> propiedad = listaPropiedadesPorIdPlugin.stream()
					.filter(fil -> fil.getName().equals(resourceVmBean.getName())).map(AddinsProp::getValue)
					.findAny();

			if (propiedad.isPresent()) {
				if ("0".equals(propiedad.get()))
					radiogroup.setSelectedItem(radiotrue);
				else
					radiogroup.setSelectedItem(radiofalse);
			}else {
				
				if ("true".equals(resourceVmBean.getSelecteditem()))
					radiogroup.setSelectedItem(radiotrue);
				else
					radiogroup.setSelectedItem(radiofalse);
			}

//		}

	}

	private void createOP() {
		plantillaColumnas();
		addLabel(label1, resourceVmBean.getName(), "");
		createComponent();
		createCombobox(resourceVmBean.getName());

		row.appendChild(combobox);
//		if (resourceVmBean.getType().equals("RESOURCE"))
//			createBotonResource(resourceVmBean.getClaseresource());
	}

	private void createBotonResource(String clase) {
		Button boton = new Button();
//		<button 
//		iconSclass="z-icon-edit"
//		sclass="btn-warning"
//		
//		onClick="@command('showCreateTeam', usuario=vm.userVmBean)" tooltiptext="New Team"/>
		boton.setIconSclass("z-icon-edit");
		boton.setSclass("btn-warning");
		boton.addEventListener(Events.ON_CLICK, (Event event) -> showWindowResource(event, clase));
		row.appendChild(boton);

	}

	private void showWindowResource(Event event, String clase) {
		Window window = null;
		final HashMap<String, Object> map = new HashMap<String, Object>();
		// wResource.detach();
		map.put("source", clase);
		window = (Window) Executions.createComponents("/zul/editarresource.zul", null, map);

		window.doModal();
	}

	private void createCombobox(String name) {
		combobox = new Combobox();
		combobox.setAttribute("nombreatt", name);

		for (Map.Entry<String, String> entry : valueLabelmap.entrySet()) {
			Comboitem item = new Comboitem();
			item.setLabel(entry.getValue());
			item.setValue(entry.getKey());

			combobox.appendChild(item);

//			if (!modificacion) {
//				log.info("name-> " + name);
//				log.info("para-> " + resourceVmBean.getSelecteditem());
//				log.info("entry.getKey()-> " + entry.getKey());
//				if (entry.getKey().equals(resourceVmBean.getSelecteditem()))
//					combobox.setSelectedItem(item);
//				// si viene el combo sin nada inicializamos con -none-
//				else if (entry.getKey().equals("-none-")) {
//
//					combobox.setSelectedItem(item);
//
//				}
//				// log.info("combobox.getSelectedIndex();->
//				// "+combobox.getSelectedIndex());
//
//			} else {

				Optional<String> propiedad = listaPropiedadesPorIdPlugin.stream()
						.filter(fil -> fil.getName().equals(resourceVmBean.getName())).map(AddinsProp::getValue)
						.findAny();

				if (propiedad.isPresent()) {
					if (entry.getKey().equals(propiedad.get())) {

						combobox.setSelectedItem(item);
					}
				}else {
					
					log.info("name-> " + name);
					log.info("para-> " + resourceVmBean.getSelecteditem());
					log.info("entry.getKey()-> " + entry.getKey());
					if (entry.getKey().equals(resourceVmBean.getSelecteditem()))
						combobox.setSelectedItem(item);
					// si viene el combo sin nada inicializamos con -none-
					else if (entry.getKey().equals("-none-")) {

						combobox.setSelectedItem(item);

					}
				}
			}
//		}

	}

	private void createS() {
		plantillaColumnas();

		textbox1 = new Textbox();
		addLabel(label1, resourceVmBean.getName(), "");
		// textbox1.setName(resourceVmBean.getName());
		textbox1.setAttribute("nombreatt", resourceVmBean.getName());
//
//		if (!modificacion) {
//			textbox1.setValue("");
//		} else {

			Optional<String> propiedad = listaPropiedadesPorIdPlugin.stream()
					.filter(fil -> fil.getName().equals(resourceVmBean.getName())).map(AddinsProp::getValue)
					.findAny();

			if (propiedad.isPresent())
				textbox1.setValue(propiedad.get());
			else
				textbox1.setValue("");	
//		}

		createComponent();
		row.appendChild(textbox1);

	}

	public Div createFormProp(String prop) {

		Div divform = new Div();
		Hlayout hlayout = new Hlayout();
		Listbox listbox = new Listbox();

		Textbox textbox1 = new Textbox();
		Button boton = new Button();

		Grid grid2 = new Grid();

		Columns columnas1 = new Columns();
		Column columna3 = new Column();
		Column columna4 = new Column();
		Rows rows = new Rows();
		Row row = new Row();

		hlayout.setSpacing("0");
		hlayout.setHeight("300px");
		hlayout.setWidth("290px");
		divform.appendChild(hlayout);
		hlayout.appendChild(listbox);

		listbox.setVflex(true);
		listbox.setHflex("1");
		listbox.setMultiple(true);

		for (int ite = 0; ite < listSkill.size(); ite++) {
			if (!listSkill.get(ite).equals(""))
				addListItem(prop, listbox, ite, null);
		}
		addListItem(prop, listbox, -1, null);

		columna3.setWidth("200px");
		columnas1.appendChild(columna3);
		columna4.setWidth("80px");
		columnas1.appendChild(columna4);

		grid2.appendChild(columnas1);
		textbox1.setValue("");
		row.appendChild(textbox1);
		boton.addEventListener(Events.ON_CLICK, (Event event) -> addItem(prop, listbox, event, textbox1.getValue()));
		boton.setIconSclass("z-icon-pencil");
		row.appendChild(boton);

		rows.appendChild(row);
		grid2.appendChild(rows);
		grid2.setWidth("290px");
		divform.appendChild(grid2);

		return divform;

	}

	private void addListItem(String prop, Listbox listbox, int ite, String skill) {
		Listitem listitem;
		listitem = new Listitem();
		Listcell listcellicon = new Listcell();
		Listcell listcelltext = new Listcell();
		// tenemos que meter un iten vacio para que nos pueda crear el componente
		// dinamicamente,
		if (ite == -1) {
			listitem.appendChild(listcelltext);
			listitem.appendChild(listcellicon);
			listitem.setDroppable("true");
			listcelltext.setAttribute("nombreatt", resourceVmBean.getName());
			listbox.appendChild(listitem);

		} else {
			// si es añadida del listado o por el usuario. si skill es !null el usuario
			// añade
			if (skill == null) {

				listcelltext.setLabel(listSkill.get(ite));

				listitem.appendChild(listcelltext);
				listcelltext.setAttribute("nombreatt", resourceVmBean.getName());

			} else {

				listcelltext.setLabel(skill);
				listitem.appendChild(listcelltext);
				listcelltext.setAttribute("nombreatt", prop);
			}
			listcellicon.addEventListener(Events.ON_CLICK, (Event event) -> deleteItem(listbox, event));

			listcellicon.setIconSclass("fa fa-trash azul");
			listitem.appendChild(listcellicon);
			listitem.setDraggable("true");
			listitem.setDroppable("true");
			listitem.addEventListener(Events.ON_DROP,
					(DropEvent event) -> moveToTop(listbox, event, event.getDragged()));
			listbox.appendChild(listitem);
		}
	}

	private void addItem(String prop, Listbox listbox, Event event, String skill) {
		log.info(skill);
		if (skill != "")
			addListItem(prop, listbox, 70, skill);

	}

	private void deleteItem(Listbox listbox, Event event) {
		Listcell item = (Listcell) event.getTarget();
		// int toDelete = ((Listitem) item.getParent()).getIndex();
		listbox.removeChild(item.getParent());

	}

	private void moveToTop(Listbox listbox, DropEvent event, Component component) {
		DropEvent dropEvent = (DropEvent) event;
		Listitem listR2 = (Listitem) dropEvent.getTarget();
		Component dragged = dropEvent.getDragged();
		if (dragged instanceof Listitem) {
			int to = listR2.getIndex();
			Component toObject = (Component) listbox.getItemAtIndex(to);
			dragged.getParent().insertBefore(dragged, toObject);
		}
	}

	private void addLabel(Label label, String value, String sclass) {
		label.setSclass(sclass);
		label.setValue(value);
	}

	private void readDataReq(List<Component> listaHijos) {

		// String atributosCelList ="";
		listaHijos.stream().forEach(hijo -> {
			propresou.clear();
			if (hijo instanceof Grid) {
				// atributosCelListAtto.set(atributosCelList);
				atributosCelList = "";
				propiedadAtto.set(propiedad);
				log.info("grid------------------------------------------" + hijo.getUuid() + "-------> " + " ---"
						+ hijo.getAttribute("nombreatt") + "---" + hijo.getAttribute("typeatt"));
				propiedadAtto.set((String) hijo.getAttribute("nombreatt"));
			}

			if (hijo instanceof Listcell) {
				// List<String> listaDeListCell = new ArrayList<>();

				if (propiedadAtto.get().equals(hijo.getAttribute("nombreatt"))) {
					// log.info("Listcell--------> " + " ---" +
					// hijo.getAttribute("nombreatt") + "---" + hijo.getAttribute("typeatt"));
					Listcell listcell = (Listcell) hijo;
					// log.info(listcell.getLabel());
					// listaDeListCell.add(listcell.getLabel());
					atributosCelList = atributosCelList + listcell.getLabel() + ";";
					// log.info(atributosCelList);
					if (!atributosCelList.equals(""))
						prop.put((String) hijo.getAttribute("nombreatt"),
								atributosCelList.substring(0, atributosCelList.length() - 1));
				}

			}

			if (hijo instanceof Textbox && propiedadAtto.get() != null) {
				if (propiedadAtto.get().equals(hijo.getAttribute("nombreatt"))) {
					Textbox textbox = (Textbox) hijo;
					// log.info("textbox--------> " + textbox.getValue());
					prop.put((String) hijo.getAttribute("nombreatt"), textbox.getValue());
				}
			}
			if (hijo instanceof Combobox) {
				if (propiedadAtto.get().equals(hijo.getAttribute("nombreatt"))) {
					Combobox combobox = (Combobox) hijo;
					// log.info("combobox--------> " + combobox.getSelectedIndex()+" "+
					// combobox.getSelectedItem().getValue());
					prop.put((String) hijo.getAttribute("nombreatt"), combobox.getSelectedItem().getValue());
				}
			}
			if (hijo instanceof Radiogroup) {
				if (propiedadAtto.get().equals(hijo.getAttribute("nombreatt"))) {
					Radiogroup radiogroup = (Radiogroup) hijo;
					// log.info("radiogroup--------> " + radiogroup.getSelectedIndex());
					prop.put((String) hijo.getAttribute("nombreatt"), radiogroup.getSelectedIndex() + "");
				}
			}

			if (hijo.getChildren() != null) {
				readDataReq(hijo.getChildren());
			}

		});

	}

	private void createComponent() {
		label2.setTooltiptext(resourceVmBean.getInfo());

		label2.setSclass("fa fa-info fa-lg azul");
		row.appendChild(label1);
		row.appendChild(label2);
		rows.appendChild(row);

		grid1.appendChild(rows);

		divcontainer.appendChild(grid1);
	}

	private void plantillaColumnas() {
		columna1 = new Column();
		columna2 = new Column();
		columna3 = new Column();
		columna4 = new Column();
		columnas = new Columns();
		grid1 = new Grid();
		rows = new Rows();
		row = new Row();
		// row2 = new Row();
		label1 = new Label();
		label2 = new Label();

		grid1.getHflex();

		columna1.setWidth("120px");
		columnas.appendChild(columna1);
		columna2.setWidth("60px");
		columna2.setAlign("center");
		columnas.appendChild(columna2);
		columna3.setWidth("300px");
		columnas.appendChild(columna3);
		columna4.setWidth("100px");
		columnas.appendChild(columna4);

		grid1.setAttribute("nombreatt", resourceVmBean.getName());
		grid1.setAttribute("typeatt", resourceVmBean.getType());
		grid1.appendChild(columnas);
	}

	private void createSL() {
		plantillaColumnas();
//		grid1.setAttribute("nombreatt", resourceVmBean.getName());
//		grid1.setAttribute("typeatt", resourceVmBean.getType());
//		grid1.appendChild(columnas);

		addLabel(label1, resourceVmBean.getName(), "label-required");

		createComponent();
		row.appendChild(createFormProp(resourceVmBean.getName()));
	}

	@Listen("onClick=#onClose")
	public void onClose() {
		divcontainer.detach();
		divcontainer = null;
		resClassProp.clear();
		comp.detach();
		// wResource.detach();
	}

	@Listen("onClick=#onSave")
	public void onSave() throws CxException {

		log.info(getType().getValue());
		// log.info(getDescripcion().getValue());

		List<Component> listaHijos = divcontainer.getChildren();

		readDataReq(listaHijos);
		
		AddinsPlg addinsplg = addins.getAddinsPlg();
		addinsplg.setId(addins.getId());
		//addinsDev.setName(getName().getValue());
		
		
//		addinsDevMag.saveOrUpdate(addinsDev);
//
		propaddinsMag.deleteByAddins(addins);
//
		for (Map.Entry<String, String> entry : prop.entrySet()) {
//
			addinsProp = new AddinsProp();
			
			if (entry.getKey() != null)

				if (entry.getValue() != null) {
					addinsProp.setAddins(addins);
					
					addinsProp.setValue(entry.getValue());
					addinsProp.setName(entry.getKey());
				}
			addinsProp = propaddinsMag.saveOrUpdate(addinsProp);
		}
//		// crear configMsg
//
//		if (!modificacion) {
//			resourceXml.createMsg(resource, modificacion);
//			// conserMag.doConfig();
//		}

		// resourceXml.updateMsg(resource, prop);
//
		log.info(addins.getId().toString());
		BindUtils.postGlobalCommand(null, null, "loadPlugin", null);
		// BindUtils.postGlobalCommand(null, null, "cargaCombosTeam", null);
		divcontainer.detach();
		divcontainer = null;
		resClassProp.clear();
		comp.detach();
	}
//	private void createND() {
//	Column columna1 = new Column();
//	Column columna2 = new Column();
//	Column columna3 = new Column();
//	Column columna4 = new Column();
//	Columns columnas = new Columns();
//	Grid grid1 = new Grid();
//	Rows rows = new Rows();
//	Row row = new Row();
//
//	Label label1 = new Label();
//	Label label2 = new Label();
//
//	Textbox textbox1 = new Textbox();
//	Textbox textbox2 = new Textbox();
//
//	grid1.getHflex();
//
//	columna1.setWidth("120px");
//	columnas.appendChild(columna1);
//	columna2.setWidth("220px");
//	columnas.appendChild(columna2);
//	columna3.setWidth("120px");
//	columnas.appendChild(columna3);
//	columna4.setWidth("220px");
//	columnas.appendChild(columna4);
//
//	grid1.appendChild(columnas);
//	grid1.setAttribute("nombreatt", "header");
//	grid1.setAttribute("typeatt", "NOBREDESCRIPCION");
//	addLabel(label1, Labels.getLabel("column.name"), "label-required");
//	row.appendChild(label1);
//	textbox1.setName("Nombre");
//	row.appendChild(textbox1);
//
//	addLabel(label2, Labels.getLabel("column.desc"), "label-required");
//	row.appendChild(label2);
//
//	rows.appendChild(row);
//	textbox2.setName("Descripcion");
//	row.appendChild(textbox2);
//	rows.appendChild(row);
//
//	grid1.appendChild(rows);
//
//	divcontainer.appendChild(grid1);
//
//}
}
