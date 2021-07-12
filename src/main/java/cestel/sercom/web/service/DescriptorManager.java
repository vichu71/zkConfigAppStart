package cestel.sercom.web.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.zkoss.util.resource.Labels;
import org.xml.sax.SAXException;

import cestel.sercom.web.descriptor.bean.ResClassPropBean;
import cestel.sercom.web.descriptor.bean.ResOptionsBean;
import cestel.sercom.web.exception.InvalidXmlDescriptorException;
import cestel.sercom.web.util.DOMUtils;
import cestel.sercom.web.util.DirectoryFilter;
import cestel.sercom.web.util.XmlFileFilter;
import cestel.sercom.web.util.XmlFileMaskFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vmhuecas
 * @since jun 2021
 */
@Slf4j
@Transactional
@Component("decriptorMag")
public class DescriptorManager {

//	@Autowired
//	private DescriptorRepositoryImpl<?> descriptorRepository;

	private static final String XSD_RESOURCES_FILE = "ResourceDescriptor.xsd";

	private static final String XSD_ADDINS_FILE = "AddinDescriptor.xsd";
	/**
	 * Objeto de acceso al FileSystem, que apunta a la Ruta Raiz de los Descriptores
	 * XML.
	 */
	private File rootDir = null;
	private String xmlHome = Labels.getLabel("CfgApp.XmlDescriptorsRootPath");
	/**
	 * Ruta del filesystem donde se encuentran los XML Scehmas para validar los
	 * Descriptores XML de Addins (Devices, Plugins) y de Resources.
	 */
	private String xsdHome = Labels.getLabel("CfgApp.XmlDescriptorsSchemaLocation");
	/**
	 * Se guarda la instancia del XML Schema utilizado para validar los Descriptores
	 * de Resources.
	 */
	private Schema xsdSchemas;

	List<ResClassPropBean> listClassprop = new ArrayList<>();
	
	List<String>  devicesMenu = new ArrayList<String>();

	public List<ResClassPropBean> cargaDescriptorResources(String ficheroXml) throws SAXException {
		log.info("Buscando Descriptores XML... ");
		// Proceso para el xml de System Properties

		rootDir = new File(xmlHome);

		setupXmlSchemas(XSD_RESOURCES_FILE);

//	        File[] sysPropXml= rootDir.listFiles(new XmlFileMaskFilter("SysPropsDesc"));
//	        if (sysPropXml.length>0)
//	          _ParseSysPropDescriptor(sysPropXml[0].getName());

		// Proceso todos los archivos Descriptores XML de Recursos
		File[] files = rootDir.listFiles(new XmlFileMaskFilter(ficheroXml));

		// Parseo cada una de ellas
		for (int i = 0; i < files.length; i++) {
			parseResourceDescFile(files[i].getName());
		}
		// Proceso todas las carpetas = familias
//	        File[] folders = rootDir.listFiles(new DirectoryFilter());
//	        // Parseo cada una de ellas
//	        for (int i = 0; i < folders.length; i++) {
//	            _ScanFamily(folders[i].getName(), i);
//	        }
		log.info("Finalizada la busqueda de Descriptores XML. ");
		return listClassprop;

	}

	public List<String> cargaDescriptorDeviceType() throws SAXException {
		log.info("Buscando cargaDescriptorDeviceType XML... ");
		// Proceso para el xml de System Properties

		rootDir = new File(xmlHome);

		setupXmlSchemas(XSD_ADDINS_FILE);

		// Proceso todas las carpetas = familias
		File[] folders = rootDir.listFiles(new DirectoryFilter());
		// Parseo cada una de ellas
		for (int i = 0; i < folders.length; i++) {
			scanFamily(folders[i].getName(), i);
		}
		log.info("Finalizada la busqueda de Descriptores XML. ");
		return devicesMenu;

	}
	
	public List<String> cargaDescriptorAddinsMenu() throws SAXException {
		log.info("Buscando cargaDescriptorAddinsMenu XML... ");
		// Proceso para el xml de System Properties

		rootDir = new File(xmlHome);

		setupXmlSchemas(XSD_ADDINS_FILE);

		// Proceso todas las carpetas = familias
		File[] folders = rootDir.listFiles(new DirectoryFilter());
		// Parseo cada una de ellas
		for (int i = 0; i < folders.length; i++) {
			scanFamily(folders[i].getName(), i);
		}
		log.info("Finalizada la busqueda de Descriptores XML. ");
		return devicesMenu;

	}


	private void scanFamily(String nameFamily, int familyPos) {

		// Obtengo todos los Archivos XML dentro de la carpeta de la Familia
		File familyFolder = new File(xmlHome + File.separator + nameFamily);
		File[] xmlFiles = familyFolder.listFiles(new XmlFileFilter());

		// proceso cada uno de los archivos XML
		for (int i = 0; i < xmlFiles.length; i++) {
			parseAddinDescFile(nameFamily, extractFileExt(xmlFiles[i].getName()));
		}

	}
    private void parseAddinDescFile(String nameFamily, String typeAndVer) {
        String filename = xmlHome + File.separator + nameFamily + File.separator + typeAndVer + ".xml";
        String type = "";
        String version = "";
        log.info(filename);
        try {
            // separo el Nombre (Tipo de Addin) de su Version
            String[] nv = splitTypeAndVersion(typeAndVer);
            type = nv[0];
            version = nv[1];

            // Abrimos el Archivo XML y obtenemos el modelo DOM de su contenido
            File xmlFile = new File(filename);
           // Element root = (Element) DOMUtils.getXmlFileRoot(xmlFile, xsdSchemas);
            Document document = DOMUtils.getXmlFileRoot(xmlFile, xsdSchemas);
            if (document == null) {
                throw new InvalidXmlDescriptorException("Error while parsing/validating."); //DescriptorsRepoBean.java-deRB03
            }
            // Obtenemos el hijo del nodo raiz (addinDescriptor), que especifica el Addin
            // (hay que buscar el primero de tipo ELEMENT)
            NodeList nodeList = document.getDocumentElement().getChildNodes();
			Element root = document.getDocumentElement();
            Node child = root.getFirstChild();
            while ((child != null) && (child.getNodeType() != Node.ELEMENT_NODE)) {
                child = child.getNextSibling();
                if (child == null) {
                    throw new InvalidXmlDescriptorException("Addin Descriptor not found (or invalid)."); //DescriptorsRepoBean.java-deRB04
                }
            }
            Element addin = (Element) child;

            // Segun el tipo de Addin, instanciamos el AddinDescriptor adecuado.
            String addinTag = addin.getNodeName();
            // Generacion de claves unicas para los Addins
          //  String Key = this._getDescriptorKey(nameFamily, type, version);
            if (("device").equals(addinTag)) {
            	
            	if(!devicesMenu.contains(addin.getAttribute("group")))
            	devicesMenu.add(addin.getAttribute("group"));
            	
                // Si es un Descriptor de Device, creamos el objeto correspondiente
                // y lo almacenamos en su contenedor; excepto que ya exista en el mismo.
//                if (devDescriptors.get(Key) == null) {
//                    DeviceDescriptor dd = new DeviceDescriptor(nameFamily, type, version, Key, xmlFile.lastModified(), addin);
//                    devDescriptors.put(Key, dd);
//                    _AddGroupAndDeviceDesc(dd.getGroup(), dd);
//                    log.info("Agregado Descriptor de Device: " + Key + " en Grupo:" + dd.getGroup());
//                    log.debug("Descriptor:" + Key + ", contiene Props = " + dd.getPropList());
//
//                }
//            } 
//            else if (addinTag.compareTo("techPlugin") == 0) {
//                // Si es un Descriptor de TechPlugin, creamos el objeto correspondiente
//                // y lo almacenamos en su contenedor; excepto que ya exista en el mismo.
//                if (tpDescriptors.get(Key) == null) {
//                    PluginDescriptor tpd = new PluginDescriptor(nameFamily, type, version, Key, xmlFile.lastModified(), addin);
//                    tpDescriptors.put(Key, tpd);
//                    log.info("Agregado Descriptor de TechPlugin: " + Key);
//                    log.debug("Descriptor:" + Key + ", contiene Props = " + tpd.getPropList());
//                }
//            } else if (addinTag.compareTo("clientPlugin") == 0) {
//                // Si es un Descriptor de ClientPlugin, creamos el objeto correspondiente
//                // y lo almacenamos en su contenedor; excepto que ya exista en el mismo.
//                if (cpDescriptors.get(Key) == null) {
//                    PluginDescriptor cpd = new PluginDescriptor(nameFamily, type, version, Key, xmlFile.lastModified(), addin);
//                    cpDescriptors.put(Key, cpd);
//                    log.info("Agregado Descriptor de ClientPlugin: " + Key);
//                    log.debug("Descriptor:" + Key + ", contiene Props = " + cpd.getPropList());
//                }
            } else {
                //throw new InvalidXmlDescriptorException("Unknown descriptor"); //DescriptorsRepoBean.java-deRB05
            }
        } catch (InvalidXmlDescriptorException ixde) {
            log.error("DescriptorsRepository: ERROR en archivo " + filename + " - Causa: " + ixde.getMessage());
           // _appLog("ERROR", "Descriptors Repository: error in file " + filename + "<br> It's recommended to notify the support personnel.");
        } catch (Exception ex) {
            log.error("DescriptorsRepository: ERROR en archivo " + filename + " - Causa: " + ex.getMessage());
          //  _appLog("ERROR", "Descriptors Repository: error in file " + filename + "<br> It's recommended to notify the support personnel.");
        }
    }

    /** Separa una cadena "Nombre_vv-ss" (donde vv=version, ss=Subversion) en las dos
     * partes: Name y Version. <br>
     * La forma de devolver el resultado es un poco rara pero vale: se devuelve un array
     * de 2 Strings, el primero es "Name" y el segundo "Version" */
    private String[] splitTypeAndVersion(String typeAndVer) throws InvalidXmlDescriptorException {
        String[] retval = {"", ""};
        int usPos = typeAndVer.indexOf('_');
        if ((usPos > 0) && (usPos < typeAndVer.length() - 5)) {
            retval[0] = typeAndVer.substring(0, usPos);
            retval[1] = typeAndVer.substring(usPos + 1);
        } else {
            throw new InvalidXmlDescriptorException("The Name of XML file (" + typeAndVer + ") must have the format: \'xxxxx_vv-ss\'."); //DescriptorsRepoBean.java-deRB06
        }
        return retval;
    }
	 private String extractFileExt(String filename) {
	        String retval = filename;
	        int dotPos = filename.indexOf('.');
	        if ((dotPos > 0) && (dotPos < filename.length())) {
	            retval = filename.substring(0, dotPos);
	        }
	        return retval;
	    }

	private void parseResourceDescFile(String FName) {
		String fullFilename = xmlHome + File.separator + FName;

		try {
//            ResourceClass resClass;
//            // extraigo el Caracter identificativo del Tipo de Resource
//            try {
//                String aux = _ExtractFileExt(FName).substring(RES_DESC_PREFIX.length()).toUpperCase();
//                resClass = ResourceClass.encode(aux.charAt(0));
//            } catch (Exception e) {
//                throw new InvalidXmlDescriptorException("Invalid filename format. " + FName + "  " + FullFilename); //DescriptorsRepoBean.java-deRB07
//            }

			// Abrimos el Archivo XML y obtenemos el modelo DOM de su contenido
			File xmlFile = new File(fullFilename);
			log.info("xmlFile XML... " + xmlFile.getName());
			Document document = DOMUtils.getXmlFileRoot(xmlFile, xsdSchemas);
			if (document == null) {
				throw new InvalidXmlDescriptorException("Error while parsing/validating."); // DescriptorsRepoBean.java-deRB08
			}
			// Verificamos el tipo de nodo-raiz
			NodeList nodeList = document.getDocumentElement().getChildNodes();
			Element root = document.getDocumentElement();
			String rootTag = root.getNodeName();
			if (rootTag.compareTo("resourceDescriptor") == 0) {
				log.info("es de tipo properties... " + rootTag);
				parseProperties(document);

			} else {
				throw new InvalidXmlDescriptorException("The file [" + FName + "]  is an unknown descriptor."); // DescriptorsRepoBean.java-deRB09
			}
		} catch (InvalidXmlDescriptorException ixde) {
			log.error("DescriptorsRepository: ERROR en archivo " + fullFilename + " - Causa: " + ixde.getMessage());
			// _appLog("ERROR", "Descriptors Repository: error in file " + FullFilename +
			// "<br> It's recommended to notify the support personnel.");
		}

	}

	private void parseParameters(Element XmlNode) throws InvalidXmlDescriptorException {
		NodeList rootNodes = XmlNode.getElementsByTagName("parameters");
		if (rootNodes.getLength() == 1 && (rootNodes.item(0).getNodeType() == Node.ELEMENT_NODE)) {
			Element propList = (Element) rootNodes.item(0);
			NodeList params = propList.getElementsByTagName("param");
			for (int i = 0; i < params.getLength(); i++) {
				Element param = (Element) params.item(i);
				// parameters.put( param.getAttribute("name"), param.getAttribute("value"));
			}
		}
	}

	private void parseProperties(Document document) throws InvalidXmlDescriptorException {

		// descriptorRepository.deleteAll();
		// Session sess = Sessions.getCurrent();

		document.getDocumentElement().normalize();

		// String raiz = document.getDocumentElement().getNodeName();
		String attq1 = document.getDocumentElement().getAttribute("classLabel");
		// String attq2 =
		// document.getDocumentElement().getAttribute("classLabelPlural");
		// Attr att1 = document.getDocumentElement().getAttributeNode("classLabel");
		// Attr att2 =
		// document.getDocumentElement().getAttributeNode("classLabelPlural");
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		// for (int prop = 0; prop < nodeList.getLength(); prop++) {
		Node nodo = nodeList.item(1);

		log.info("Elemento:" + nodo.getNodeName());
		Element element = (Element) nodo;

		NodeList nodeListSon = element.getChildNodes();

		for (int prop = 0; prop < nodeListSon.getLength(); prop++) {
			ResClassPropBean resClassProp = new ResClassPropBean();
			resClassProp.setClase(attq1);
			Node nodeson = nodeListSon.item(prop);
			if (nodeson.getNodeType() == Node.ELEMENT_NODE) {
				Element element1 = (Element) nodeson;
				// System.out.print("nodeson name: " + nodeson.getNodeName() + " ");

				// System.out.print(element1.getAttributeNode("name").getNodeValue() + " ");
				String name = element1.getAttributeNode("name").getNodeValue();
				if (name != null)
					resClassProp.setName(name);
				String type = element1.getAttributeNode("type").getNodeValue();
				if (type != null)
					resClassProp.setType(type);
				// System.out.print(element1.getAttributeNode("type").getNodeValue() + " ");
				if (element1.getAttributeNode("advanced") != null) {
					String avanced = element1.getAttributeNode("advanced").getNodeValue();

					resClassProp.setAdvanced(Boolean.parseBoolean(avanced));
					// log.info(element1.getAttributeNode("advanced").getNodeValue() + "
					// ");
				}
				NodeList nodeListSon1 = element1.getChildNodes();

				for (int prop1 = 0; prop1 < nodeListSon1.getLength(); prop1++) {

					Node nodeson1 = nodeListSon1.item(prop1);
					if (nodeson1.getNodeType() == Node.ELEMENT_NODE) {
						Element element2 = (Element) nodeson1;
						// System.out.print("nodeson1 name: " + nodeson1.getNodeName() + " ");

						// System.out.print(element2.getTextContent());
						if (nodeson1 != null && !nodeson1.getNodeName().equals("desc")) {
							log.info(element2.getAttributeNode("defVal") + "  ");
							resClassProp.setDefval(element2.getAttributeNode("defVal").getNodeValue());
							if (element2.getAttributeNode("resClassCode") != null) {
								resClassProp.setResClassCode(element2.getAttributeNode("resClassCode").getNodeValue());
							} else {
								resClassProp.setResClassCode("");
							}
						} else {
							resClassProp.setDescripcion(element2.getTextContent());
						}
						resClassProp.setResOptions(null);
						if (nodeson1 != null && nodeson1.getNodeName().equals("optionRange")) {

							List<ResOptionsBean> opciones = new ArrayList<ResOptionsBean>();
							NodeList nodeListSon2 = element2.getChildNodes();
							for (int prop2 = 0; prop2 < nodeListSon2.getLength(); prop2++) {
								Node nodeson2 = nodeListSon2.item(prop2);

								if (nodeson2.getNodeType() == Node.ELEMENT_NODE) {
									ResOptionsBean opcion = new ResOptionsBean();
									Element element3 = (Element) nodeson2;
									// System.out.print("options name: " + nodeson2.getNodeName() + " ");
									// log.info("options name: " + element3.getAttribute("value") + " ");
									opcion.setValue(element3.getAttribute("value"));
									opcion.setLabel(element3.getAttribute("label"));
									opcion.setIdprop(Long.valueOf(prop));

									opciones.add(opcion);

								}
							}
							resClassProp.setOptionrange(Long.valueOf(prop));
							resClassProp.setResOptions(opciones);

						}

					}
				}
				// descriptorRepository.save(resClassProp);
				listClassprop.add(resClassProp);
				// sess.setAttribute("resClassProp", resClassProp);

			}

		}

	}

	/**
	 * Obtencion de los XML Schemas de para validacion de Descriptores de Addins y
	 * Resources.
	 * 
	 * @throws SAXException
	 */
	private void setupXmlSchemas(String xsdFile) throws SAXException {
		String aux = "http://www.w3.org/2001/XMLSchema";
		log.info("Creando SchemaFactory [" + aux + "]");
		SchemaFactory sf = SchemaFactory.newInstance(aux);

		xsdSchemas = sf.newSchema(new File(xsdHome + File.separator + xsdFile));
		// xsdResources = sf.newSchema(new File(xsdHome + File.separator +
		// XSD_RESOURCES_FILE));
//        xsdSysProps = sf.newSchema(new File(xsdHome + File.separator + XSD_SYSPROPS_FILE));
	}

}