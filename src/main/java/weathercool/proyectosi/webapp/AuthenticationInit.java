package weathercool.proyectosi.webapp;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Initiator;
import weathercool.proyectosi.User;

import java.util.Map;

public class AuthenticationInit implements Initiator {
    AuthenticationService authService = AuthenticationService.getInstance();

    public void doInit(Page page, Map<String, Object> args) throws Exception {
        if (!authService.isLoguedIn()){
            Executions.sendRedirect("/login.zul");
            return;
        }
    }
}