package cestel.sercom.web.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Window;

import cestel.sercom.web.exception.CxException;
import cestel.sercom.web.service.ConexionSercomManager;
import cestel.sercom.web.vm.bean.UserVmBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.io.IOException;
import java.util.Set;


@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class LoginVm extends Window {

	
	 private Validator validator;
	 @Getter
	 private UserVmBean userlogin;
	 
	 @Getter
	 @Setter
	 private String error;
	 
	 @Getter
	 @Setter
	 private String	 errorDominio;
	 
	 @WireVariable
	private ConexionSercomManager conserMag;
 
    /**
     * Called once on upon the initial request.
     */
    @Init
    public void init() {

        // Get all the collaborator beans from the Spring application context.
//    	try {
//			conserMag.isCoreConfigured();
//		} catch (CxException e) {
//			log.error("error de conexion con sercom");
//			e.printStackTrace();
//		}
//      
        this.validator = SpringUtil.getApplicationContext().getBean(Validator.class);
        this.userlogin = new UserVmBean();

       // System.out.println("[Edit VM] Initialized view-model with use case instance: {}");
    }
    @AfterCompose
    public void doAfterComposer() {
    	
    	 	Execution exec = Executions.getCurrent();
    	    String param=exec.getParameter("error");
    	    this.error = param;
    	    String paramDominio=exec.getParameter("errorDominio");
    	    this.errorDominio = paramDominio;
    	    
    }
    @Command
	public void logon() throws IOException {
    	 Set<ConstraintViolation<UserVmBean>> validationErrors = this.validator.validate(this.userlogin);
    	if (!validationErrors.isEmpty()) {
            this.handleValidationErrors(validationErrors);
            return;
        }
    	Session sess = Sessions.getCurrent();
    	 sess.setAttribute("dominio", String.valueOf( userlogin.getDominio()));
    	Clients.submitForm("formLogin");
    }
   

    private void handleValidationErrors(Set<ConstraintViolation<UserVmBean>> validationErrors) {
        // just show the first error
        validationErrors.stream().findAny()
                .ifPresent(error -> showError(error.getPropertyPath() + ": " + error.getMessage()));
    }

//    private void showInfo(String info) {
//        Notification.show(info,
//                Notification.TYPE_INFO, null,
//                400,
//                400, 2000, false);
//    }

    private void showError(String error) {
        Notification.show(error,
                Notification.TYPE_ERROR, null,
                400,
                400, 2000, false);
    }
}
