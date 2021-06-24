package cestel.sercom.web.bean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.zkoss.util.resource.Labels;

import cestel.sercom.web.exception.CxException;
import cestel.sercom.web.repository.impl.ConexionSercomImpl;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class ConexionSercom implements ConexionSercomImpl {
	
	boolean coreAvailable = true;
	
	boolean statusLastOperation = false;
	
	boolean firstCfgCheckEmulation = true;

	public ConexionSercom() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Verifica si el core se encuentra configurado. Se realiza peticion get
	 * contra el servicio REST del core. Business method
	 * 
	 * @ejb.interface-method view-type = "local"
	 */
	@Override
	public boolean isCoreConfigured() throws CxException {
		boolean retval;
		if (coreAvailable) {

			// Hacemos peticion GET sobre consulta de si esta el core
			// configurado.
			// Se utiliza la url base del servicio mas el contexto de la
			// operacion.
			String urlBase = Labels.getLabel("CfgApp.RestConfigServiceUrl") + "/GetConfigured";

			try {
				URL uRequest = new URL(urlBase);
				HttpURLConnection uCon = (HttpURLConnection) uRequest.openConnection();
				uCon.setRequestMethod("GET");
				uCon.setReadTimeout(3000);
				uCon.setConnectTimeout(1000);
				BufferedReader reader = new BufferedReader(new InputStreamReader(uCon.getInputStream()));
				String response = reader.readLine();
				retval = (response != null && response.equals("1") ? true : false);
				statusLastOperation = true;
			} catch (Exception ex) {
				statusLastOperation = false;
				throw new CxException("Error in IsConfigured operation - Cause: " + ex.getMessage());// CoreCxBean.java-cocb02
			}
		} else {
			// para emular al Core respondemos que NO esta configurado la 1a vez
			// con lo cual se genera la ConfigInicial. En adelante decimos que
			// si.
			if (firstCfgCheckEmulation) {
				retval = false;
				firstCfgCheckEmulation = false; // nunca mas responderemos:
													// false
			} else
				retval = true;
		}
		return retval;
	}

	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub
		return statusLastOperation;
	}
	
	@Override
	public String doConfig(String XmlCfg) throws CxException {
		String retval = "";
		log.debug("(CoreCXBean) Core's XmlCfg formateado:" + XmlCfg);
//		if (_coreAvailable) {
			if (true) {
			// Hacemos peticion POST para enviar la configuracion del sistema.
			// Se utiliza la url base del servicio mas el contexto de la
			// operacion.
			String urlBase = Labels.getLabel("CfgApp.RestConfigServiceUrl") + "/ProcConfigMsg";
			log.info("urlBase " + urlBase);
			try {
				URL uRequest = new URL(urlBase);
				HttpURLConnection uCon = (HttpURLConnection) uRequest.openConnection();
				uCon.setRequestMethod("POST");
				uCon.setConnectTimeout(1000);
				uCon.setReadTimeout(3000);
				// enviamos la informacion
				uCon.setDoOutput(true);
				DataOutputStream write = new DataOutputStream(uCon.getOutputStream());
				write.writeBytes(XmlCfg); // Se envia en RAW la informacion, en
											// concreto 1 indicando que esta
											// configurado.
				write.flush();
				write.close();
				log.info("procConfigMsg Response " + uCon.getResponseCode());
				BufferedReader in = new BufferedReader(new InputStreamReader(uCon.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				retval = response.toString();
				statusLastOperation = true;
			} catch (Exception ex) {
				statusLastOperation = false;
				throw new CxException("Error in ProcConfigMsg operation - Cause: " + ex.getMessage());
			}
		} else {
			// Para emular al Core debo crear respuestas a todos los ConfigMsg
		//	retval = _emulateConfigMsgReplies(XmlCfg);
		}
		log.debug("(CoreCXBean) XML configuration msg sent to Core:" + XmlCfg);
		log.debug("(CoreCXBean) Core's reply msg:" + retval);
		return retval;
	}


}
