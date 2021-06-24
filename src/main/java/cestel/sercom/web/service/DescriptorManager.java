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

import cestel.sercom.web.entity.ResClassProp;
import cestel.sercom.web.entity.ResOptions;
import cestel.sercom.web.exception.InvalidXmlDescriptorException;
import cestel.sercom.web.repository.impl.DescriptorRepositoryImpl;
import cestel.sercom.web.util.DOMUtils;
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

	@Autowired
	private DescriptorRepositoryImpl<?> descriptorRepository;

	private static final String XSD_RESOURCES_FILE = "ResourceDescriptor.xsd";
	/**
	 * Objeto de acceso al FileSystem, que apunta a la Ruta Raiz de los Descriptores
	 * XML.
	 */
	private File rootDir = null;
	private String xmlHome = "";
	/**
	 * Ruta del filesystem donde se encuentran los XML Scehmas para validar los
	 * Descriptores XML de Addins (Devices, Plugins) y de Resources.
	 */
	private String xsdHome = "";
	/**
	 * Se guarda la instancia del XML Schema utilizado para validar los Descriptores
	 * de Resources.
	 */
	private Schema xsdResources;

	List<ResClassProp> listClassprop = new ArrayList<>();

	public List<ResClassProp> cargaDescriptor(String ficheroXml) throws SAXException {
		log.info("Buscando Descriptores XML... ");
		// Proceso para el xml de System Properties
		xsdHome = Labels.getLabel("CfgApp.XmlDescriptorsSchemaLocation");
		xmlHome = Labels.getLabel("CfgApp.XmlDescriptorsRootPath");

		rootDir = new File(xmlHome);

		setupXmlSchemas();

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

	private void parseResourceDescFile(String FName) {
		String FullFilename = xmlHome + File.separator + FName;

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
			File xmlFile = new File(FullFilename);
			log.info("xmlFile XML... " + xmlFile.getName());
			Document document = DOMUtils.getXmlFileRoot(xmlFile, xsdResources);
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
			log.error("DescriptorsRepository: ERROR en archivo " + FullFilename + " - Causa: " + ixde.getMessage());
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

		System.out.println("Elemento:" + nodo.getNodeName());
		Element element = (Element) nodo;

		NodeList nodeListSon = element.getChildNodes();

		for (int prop = 0; prop < nodeListSon.getLength(); prop++) {
			ResClassProp resClassProp = new ResClassProp();
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
					// System.out.println(element1.getAttributeNode("advanced").getNodeValue() + "
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
							System.out.println(element2.getAttributeNode("defVal") + "  ");
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

							List<ResOptions> opciones = new ArrayList<ResOptions>();
							NodeList nodeListSon2 = element2.getChildNodes();
							for (int prop2 = 0; prop2 < nodeListSon2.getLength(); prop2++) {
								Node nodeson2 = nodeListSon2.item(prop2);

								if (nodeson2.getNodeType() == Node.ELEMENT_NODE) {
									ResOptions opcion = new ResOptions();
									Element element3 = (Element) nodeson2;
									// System.out.print("options name: " + nodeson2.getNodeName() + " ");
									// System.out.println("options name: " + element3.getAttribute("value") + " ");
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
				// System.out.println("guardar aqui1-> " + resClassProp);
			}
			// System.out.println("guardar aqui2-> "+resClassProp);
		}

		// System.out.println(resClassProp);
	}

	/**
	 * Obtencion de los XML Schemas de para validacion de Descriptores de Addins y
	 * Resources.
	 * 
	 * @throws SAXException
	 */
	private void setupXmlSchemas() throws SAXException {
		String aux = "http://www.w3.org/2001/XMLSchema";
		log.info("Creando SchemaFactory [" + aux + "]");
		SchemaFactory sf = SchemaFactory.newInstance(aux);

//        xsdAddins = sf.newSchema(new File(xsdHome + File.separator + XSD_ADDINS_FILE));
		xsdResources = sf.newSchema(new File(xsdHome + File.separator + XSD_RESOURCES_FILE));
//        xsdSysProps = sf.newSchema(new File(xsdHome + File.separator + XSD_SYSPROPS_FILE));
	}

}