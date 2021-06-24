package cestel.sercom.web.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigMsgUtils {
	
	Map<String, String> relacionClassTag = new HashMap<String, String>();
	
	public ConfigMsgUtils(){
		relacionClassTag.put("T", "Team");
		relacionClassTag.put("S", "Service");
		relacionClassTag.put("C", "Channels");
		relacionClassTag.put("Q", "Queues");
		relacionClassTag.put("A", "Answers");
		relacionClassTag.put("R", "Alarms");			
	}

}
