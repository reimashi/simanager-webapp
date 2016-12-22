package weathercool.proyectosi.webapp;

import weathercool.proyectosi.User;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AuthenticationService implements Serializable {
    private static AuthenticationService instance = null;
    protected AuthenticationService() {}

    public static AuthenticationService getInstance() {
        if(instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    private User userInfo = null;
    public User getUserInfo() { return this.userInfo; }

    private boolean loguedIn = false;
    public boolean isLoguedIn() { return loguedIn; }

    public boolean login(String nm, String pd) {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        User user = em.find(User.class, nm);

        if(user == null || !user.checkPassword(pd)){
            this.userInfo = null;
            this.loguedIn = false;
            return false;
        }
        else {
            this.userInfo = user;
            this.loguedIn = true;
            return true;
        }
    }

    public void logout() {
        this.userInfo = null;
        this.loguedIn = false;
    }
}