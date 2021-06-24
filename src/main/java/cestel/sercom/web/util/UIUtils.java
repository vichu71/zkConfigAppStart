package cestel.sercom.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

//import cestel.zk.auth.SessionManager;
//import cestel.zk.base.DesktopBaseVM;

public class UIUtils {

	public static final String WND_ATTR_URI 	= "wnd_attr_uri";
	public static final String WND_URI 			= "windowUri";
	public static final String WND_ATTR_LEFT 	= "wnd_attr_left";
	public static final String WND_ATTR_TOP 	= "wnd_attr_top";
	public static final String WND_ATTR_HEIGHT 	= "wnd_attr_height";
	public static final String WND_ATTR_WIDTH 	= "wnd_attr_width";
	public static final String EVT_ON_SHOW		= "onShow";
	public static final String EVT_ON_HIDE		= "onHide";
	public static final String EVT_ON_CLOSE		= "onClose";
	public static final String VM_INIT_DATA		= "vmInitData";

	
	static final transient Logger log = LoggerFactory.getLogger(UIUtils.class);
	
	/**
	 * Oculta los paths de componentes (normalmente ventanas) que se le pasa
	 * @param component
	 */
	public static void hide(String[] paths) {
		for ( String path : paths ) {
			Window window = (Window) Path.getComponent(path);
			if ( window != null && window.isVisible() ) {
				hide(window);
			}
		}
	}
	
	
	/**
	 * Envía un evento {@link #EVT_ON_HIDE} al componente indicado
	 * @param c
	 */
	public static void hide(Component c) {
		if ( c != null ) {
			Events.sendEvent(new Event(EVT_ON_HIDE,c));
		}
	}

	/**
	 * Envía un evento {@link #EVT_ON_CLOSE} al componente indicado
	 * @param c
	 */
	public static void close(Component c) {
		if ( c != null ) {
			Events.sendEvent(new Event(EVT_ON_CLOSE,c));
		}
	}

	/**
	 * Oculta las ventanas hijas
	 * @param component para fluid call
	 */
	public static Component hideChildren(Component component) {
		component.getChildren().stream().filter(c -> c.isVisible()).forEach(c -> Events.sendEvent(new Event(EVT_ON_HIDE,c)));
		return component;
	}

	/**
	 * Muestra la ventana indicada por la uri (p.e. "/index.zul").
	 * Importante: requiere que la ventana padre tenga ID especificado.
	 * @param uri uri de la página
	 * @param parent Component path que será su parent. Sirve para mostrar ventanas dentro de otras.
	 * 			     Puede ser null 
	 * @param vmInitData dato opcional que se pasa a la página en su inicialización o evento onShow asociado al ViewModel
	 * @param parameters Mapa de parámetros adicionales 
	 * @return ventana creada
	 */
	public static Window show(String uri, Component parent, Object vmInitData, Map parameters) {
		Window window = null;
		
		// Añadir el vmInitData a los parámetros
		if ( parameters == null ) {
			parameters = new HashMap<String, Object>();
		}
		parameters.put(UIUtils.VM_INIT_DATA, vmInitData);
		
		Desktop desktop = Executions.getCurrent().getDesktop();
		
		// Añadir la url URLCodificada al bookmark
		try {
			desktop.setBookmark(URLEncoder.encode(uri,StandardCharsets.UTF_8.toString()));
		} catch (UnsupportedEncodingException e) {
			log.warn("desktop.setBookmark({}) " + e.getMessage(), uri);
		}

		/**
		 * Lazy load de la ventana de respuesta
		 * 
		 * Se crea una clave formada por WND_PATH_ + uri + parent para guardar el path de la ventana
		 * creada en el desktop. Esto permitirá buscarla posteriormente para enviarle los
		 * eventos de mostrar, ocultar o close
		 */
		String uuid = parent != null ? parent.getUuid() : "0";
		// Crear una clave de atributo única para guardar la ventana en el desktop
		String wnd_attr_uri = String.format("WND_PATH_%s_%s", uuid,uri);
		String path = (String) desktop.getAttribute(wnd_attr_uri);
		if ( path == null ) {
			// Primera vez. Creación
			window = (Window)Executions.createComponents(uri, parent, parameters);
			path = Path.getPath(window);
			// Guardamos el path en el desktop para comprobar luego si la ventana existe
			desktop.setAttribute(wnd_attr_uri, path);
			/* Guardar la clave en la ventana para poder luego 
			 * borrar el atributo de desktop en caso de cierre, preguntando
			 * al desktop por la clave de esta ventana (para obtener su path).
			 * Es decir, al cerrar la ventana, haremos así:
			 * self.getDesktop().removeAttribute((String) self.getAttribute(UIUtils.WND_ATTR_URI));
			 */
			window.setAttribute(UIUtils.WND_ATTR_URI,wnd_attr_uri);
			// log.trace("window.setAttribute({},{})",WND_ATTR_URI,wnd_attr_uri);
			
			// Resetear el uri de la ventana si se crea con este método
			window.setAttribute(UIUtils.WND_URI,uri);
			
			// Posicionamiento
			String pos = (String) parameters.get("left");
			if ( pos != null ) window.setLeft(pos);
			pos = (String) parameters.get("top");
			if ( pos != null ) window.setTop(pos);
						
			log.debug("show({}): init created on {}",new Object[]{uri,path} ); 
		} else {
			window = (Window) Path.getComponent(path);
			// Si existe, se envía el evento
			if ( window != null ) {
				String pos = (String) parameters.get("left");
				if ( pos != null ) window.setLeft(pos);
				pos = (String) parameters.get("top");
				if ( pos != null ) window.setTop(pos);
				Event event = new Event(UIUtils.EVT_ON_SHOW,window,parameters);
				log.trace("show({}): sending {} to {}, params={}",new Object[]{uri,event,path,parameters});
				Events.sendEvent(event);
			} else {
				log.warn("show({}): Path {} DOESN'T EXIST",new Object[]{uri,path});
			}
		}
		return window;
	}

	/**
	 * Muestra la ventana indicada por la uri (p.e. "/index.zul").
	 * Importante: requiere que la ventana padre tenga ID especificado.
	 * @param uri uri de la página
	 * @param parent Component path que será su parent. Sirve para mostrar ventanas dentro de otras.
	 * 			     Puede ser null 
	 * @param vmInitData dato opcional que se pasa a la página en su inicialización o 
	 * evento onShow asociado al ViewModel
	 * 
	 * @return ventana creada
	 */
	public static Window show(String uri, Component parent, Object vmInitData) {
		return show(uri, parent, vmInitData, null);
	}

	/**
	 * Devuelve el objeto ViewModel vinculado al componente que se le pasa. La instancia del objeto ViewModel
	 * está guardada como un atributo del componente con nombre "$VM$" y también con combre "vm"
	 * @param wnd
	 * @return
	 */
	public static Object getVM(Component wnd) {
		return wnd.getAttribute("$VM$");
	}
	
	
	/**
	 * Devuelve el objeto ViewModel vinculado al path que se le pasa. La instancia del objeto ViewModel
	 * está guardada como un atributo del componente con nombre "$VM$" y también con combre "vm"
	 * @param wnd
	 * @return
	 */
	public static Object getVM(String path) {
		return getVM(Path.getComponent(path));
	}

	/**
	 * Equivale a {@link BindUtils.postNotifyChange(null,null,Object,String)} pero permite notificar 
	 * a más de una propiedad indicándolo vía array. Por ejemplo: postNotifyChange(EventQueues.SESSION, this, new String[]{"lista","seleccionado"});
	 * @param queueScope
	 * @param bean
	 * @param properties
	 */
	public static void notifyChange(String queueScope, Object bean, String... properties) {
		for ( String property : properties ) {
			BindUtils.postNotifyChange(null, queueScope, bean, property.trim());			
		}
	}

	
	/**
	 * Equivale a {@link #postNotifyChange(Object, String[])} con varargs
	 * @param bean
	 * @param properties
	 */
	public static void notifyChange(Object bean, String ... properties) {
		notifyChange(null, bean, properties);
	}
	
	/**
	 * Abre una nueva pestaña con el target especificado en name
	 * @param file pagina a abrir con url path relativo a /
	 * @param name target del window.open()
	 */
	public static void open(String file, String name) {
		String js = String.format("window.open('%1$s/%2$s','%3$s')",
				Executions.getCurrent().getContextPath(),file,name);
		log.trace("[JS] {}",js);
		Clients.evalJavaScript(js);
	}


	/**
	 * Llama a {@link #open(String, Integer, Integer, Integer, Integer)
	 * con opts=null
	 * @param file
	 * @param left
	 * @param top
	 * @param height
	 * @param width
	 */
	public static void open(String file, Integer left, Integer top, Integer height, Integer width) {
		open(file, left, top, height, width, null);
	}


	/**
	 * Llama a {@link #open(String, String, Integer, Integer, Integer, Integer, String, String)}
	 * con name=file y queryString=null
	 * @param file
	 * @param left
	 * @param top
	 * @param height
	 * @param width
	 * @param opts
	 */
	public static void open(String file, Integer left, Integer top, Integer height, Integer width, String opts) {
		open(file, file, left, top, height, width, opts, null);
	}

	/**
	 * Abre un fichero en un nuevo browser
	 * @param file Fichero
	 * @param name Nombre ventana
	 * @param left Posición izq
	 * @param top Posición arriba
	 * @param height Altura
	 * @param width Ancho
	 * @param opts Opciones a open.window. 
	 * 	Se añaden a las siguientes opciones: directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,personalbar=false 
	 * @param queryString QueryString para la ventana
	 */
	public static void open(String file, String name, Integer left, Integer top, Integer height, Integer width, String opts, String queryString) {
		StringBuilder options = new StringBuilder();
		options.append("directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,personalbar=false");
		if ( left != null ) options.append(",left=").append(left);
		if ( top != null )  options.append(",top=").append(top);
		if ( height != null ) options.append(",height=").append(height);
		if ( width != null ) options.append(",width=").append(width);
		if ( opts != null ) options.append(",").append(opts);
		if ( queryString == null ) queryString = "";
		String js = String.format("window.open('%1$s/%2$s?detached=cestel%5$s','%3$s','%4$s')",
				Executions.getCurrent().getContextPath(),file,name,options,queryString);
		log.trace("[JS] {}",js);
		Clients.evalJavaScript(js);
	}


	/**
	 * Redimensiona el browser al tamaño de la ventana (si la ventana tiene espeificado tamaño o, 
	 * si no, a los valores por defecto
	 * @param wnd
	 * @param defaultX
	 * @param defaultY
	 */
//	public static void resetSize(Window wnd, int defaultX, int defaultY) {
//		int x = NumberUtils.toInt(StringUtils.removeEnd(wnd.getWidth(),"px"), defaultX);
//		int y = NumberUtils.toInt(StringUtils.removeEnd(wnd.getHeight(),"px"), defaultY); 
//		Clients.resizeTo(x,y);
//	}

	/**
	 * Devuelve el URI de una ventana creada con {@link #show(String, Component, Object)}
	 * @param wnd
	 * @return
	 */
	public static String getUri(Component wnd) {
		return (String) wnd.getAttribute(WND_URI);
	}
	
	/**
	 * Envía evento global de cierre a todos los VM que implementen {@link DesktopBaseVM}
	 */
	public static void sendCloseBrowser() {
		try {
			BindUtils.postGlobalCommand(null, EventQueues.SESSION, "closeBrowser", null);
		} catch ( Throwable t ) {
			//
		}
	}
	
	/**
	 * Envia una orden de cierre de la ventana del browser (window.close())
	 */
	public static void closeBrowserWindow() {
		Clients.evalJavaScript("window.close()");		
	}
}
