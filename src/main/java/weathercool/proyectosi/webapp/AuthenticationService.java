package weathercool.proyectosi.webapp;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import weathercool.proyectosi.User;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationService implements Serializable {
    private static AuthenticationService instance = null;
    protected AuthenticationService() {}

    public static AuthenticationService getInstance() {
        if(instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    private static Logger logger = Logger.getLogger(AuthenticationService.class.getName());
    private static final String SESSION_AUTH_TOKEN = "authToken";

    public User getUserInfo() {
        Session sess = Sessions.getCurrent();
        if (sess.hasAttribute(SESSION_AUTH_TOKEN)) return (User)sess.getAttribute(SESSION_AUTH_TOKEN);
        else return null;
    }

    public boolean isLoguedIn() { return getUserInfo() != null; }

    public boolean login(String nm, String pd) {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        User user = em.find(User.class, nm);
        Session sess = Sessions.getCurrent();

        if(user == null || !user.checkPassword(pd)){
            logger.log(Level.INFO, "Intento de sesión fallido por el usuario " + nm);
            return false;
        }
        else {
            logger.log(Level.INFO, "El usuario " + user.getUsername() + " ha iniciado sesión.");
            sess.setAttribute(SESSION_AUTH_TOKEN, user);
            return true;
        }
    }

    public void logout() {
        Session sess = Sessions.getCurrent();
        logger.log(Level.INFO, "El usuario " + getUserInfo().getUsername() + " ha cerrado sesión.");
        sess.removeAttribute(SESSION_AUTH_TOKEN);
    }
}