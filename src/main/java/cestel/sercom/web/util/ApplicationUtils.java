package cestel.sercom.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;


public final class ApplicationUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationUtils.class);

    private ApplicationUtils() {
        throw new AssertionError();
    }

    /**
     * Display an information message to the user using ZK notifications
     *
     * @param propertyKey The resource bundle property key that hold the message
     * @param arguments A list of arguments to be replaced in the message
     */
    public static void showInfo(String propertyKey, Object... arguments) {
        final String mess = Labels.getLabel(propertyKey, arguments);
        Clients.showNotification(mess, Clients.NOTIFICATION_TYPE_INFO, null, "top_right", 5000);
        logger.info(mess);
    }

    /**
     * Display a warning message to the user using ZK notifications
     *
     * @param propertyKey The resource bundle property key that hold the message
     * @param arguments A list of arguments to be replaced in the message
     */
    public static void showWarning(String propertyKey, Object... arguments) {
        final String mess = Labels.getLabel(propertyKey, arguments);
        Clients.showNotification(mess, Clients.NOTIFICATION_TYPE_WARNING, null, "top_right", 5000);
        logger.info(mess);
    }

    /**
     * Display an error message to the user using ZK notifications
     *
     * @param propertyKey The resource bundle property key that hold the message
     * @param arguments A list of arguments to be replaced in the message
     */
    public static void showError(String propertyKey, Object... arguments) {
        final String mess = Labels.getLabel(propertyKey, arguments);
        Clients.showNotification(mess, Clients.NOTIFICATION_TYPE_ERROR, null, "top_right", 5000);
        logger.info(mess);
    }
    
    

}
