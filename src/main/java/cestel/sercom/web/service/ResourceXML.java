package cestel.sercom.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cestel.sercom.web.entity.PropResource;
import cestel.sercom.web.entity.Resource;
import cestel.sercom.web.service.imp.ResourceXMLImpl;
import cestel.sercom.web.util.ConfigMsgUtils;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component("resourceXml")
public class ResourceXML implements ResourceXMLImpl {
	
	/** Cabecera del "protocolo" ConfigMsg entre CfgApp -> Core. */
	public static final String CONFIGMSG_HEAD = "<?xml version='1.0' encoding='UTF-8'?><configMsg xmlns='http://www.cestel.es/SERCOM/Core/Config'>\n\n";
	/** Cola del "protocolo" ConfigMsg entre CfgApp y el Core. */
	public static final String CONFIGMSG_TAIL = "\n</configMsg>";
	ConfigMsgUtils classValueConfig;

	public ResourceXML() {
		super();
		this.classValueConfig = new ConfigMsgUtils();
		
	}

	@Override
	public String deleteMsg(Resource resource) {
		composeDeleteMsg(resource);
		return null;
	}

	private void composeDeleteMsg(Resource resource) {
		String retval = CONFIGMSG_HEAD+"<";
		retval += getDeleteTag(resource.getResclass());
		retval += composeDeleteReqAttributes(resource); 
		retval += "/>";
		retval += CONFIGMSG_TAIL;
		log.info("retval-> " + retval);
		
	}

	private String composeDeleteReqAttributes(Resource resource) {
		StringBuilder retval = new StringBuilder();
		retval.append(" id=");
		retval.append("'");
		retval.append(resource.getId());
		retval.append("'");
		return retval.toString();
	}

	@Override
	public String updateMsg(Resource resource, Map<String, String> prop) {
		return composeUpdateMsg(resource, prop);
		
	}

	private String composeUpdateMsg(Resource resource, Map<String, String> prop) {
		String retval = CONFIGMSG_HEAD+"<";
		retval += getUpdateTag(resource.getResclass());
		retval += composeReqAttributes(resource); 
		retval += " longName='" + resource.getInfo() + "' >";
		retval += composeProperties(prop);
		retval += getCloseUpdateTag(resource.getResclass());
		retval += CONFIGMSG_TAIL;
		log.info("retval-> " + retval);
		
		return retval;
	}

	@Override
	public String createMsg(Resource resource, Boolean modificacion) {
		return composeCreaMsg(resource);
		
	}

	private String composeCreaMsg(Resource resource) {
		String retval = CONFIGMSG_HEAD+"<";
		retval += getCreateTag(resource.getResclass());
		retval += composeReqAttributes(resource); 
		retval += "/>";
		retval += CONFIGMSG_TAIL;
		log.info("retval-> " + retval);
		return retval;
		
	}

	private String composeReqAttributes(Resource resource) {
		StringBuilder retval = new StringBuilder();

		retval.append(" id='");
		retval.append(resource.getId());
		retval.append("' name='");
		retval.append(resource.getName());
		retval.append("' domain='");
		retval.append(resource.getSubdomid().getDominio().getName());
		retval.append("' subDomain='");
		retval.append(resource.getSubdomid().getName());
		retval.append("'");

		return retval.toString();
	}

//	protected String composeMsg(Resource resource, Map<String, String> prop, boolean IsModif) {
//		String retval = "<";
//		if (!IsModif)
//			retval += getCreateTag(resource.getResclass());
//		else
//			retval += getUpdateTag(resource.getResclass());
//
//		retval += composeReqAttributes(resource); // re-utilizo el metodo de la clase base
//
//		if (!IsModif)
//			retval += " />";
//		else {
//			retval += " longName='" + resource.getInfo() + "' >";
//
//			retval += composeProperties(prop);
//		}
//		log.info("retval-> " + retval);
//		return retval;
//	}

	private String getUpdateTag(String resclass) {

		return "edit" + classValueConfig.getRelacionClassTag().get(resclass);
		// return null;
	}
	private String getCloseUpdateTag(String resclass) {

		return "</edit" + classValueConfig.getRelacionClassTag().get(resclass)+">";
		// return null;
	}

	private String getCreateTag(String resclass) {
		return "create" + classValueConfig.getRelacionClassTag().get(resclass);
	}
	private String getDeleteTag(String resclass) {
		return "delete" + classValueConfig.getRelacionClassTag().get(resclass);
	}

	private String composeProperties(Map<String, String> prop) {

		StringBuilder retval = new StringBuilder(256);
		retval.append("<propertyList>");
		prop.forEach((key, value) -> {
			retval.append("<property name='" + key + "' value='");
			retval.append(value + "' />");

		});

		retval.append("</propertyList>");
		return retval.toString();
	}

}
