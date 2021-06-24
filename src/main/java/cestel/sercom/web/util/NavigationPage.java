package cestel.sercom.web.util;

import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public abstract class NavigationPage {
    private String title;
    private String includeUri;
    private String subTitle;
    private Object data;
     
    public NavigationPage(String title, String subTitle, String includeUri, Object data) {
        super();
        this.title = title;
        this.subTitle = subTitle;
        this.includeUri = includeUri;
        this.data = data;
    }
 
    public abstract boolean isSelected();
}
