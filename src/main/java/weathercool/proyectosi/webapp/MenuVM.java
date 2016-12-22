package weathercool.proyectosi.webapp;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;

public class MenuVM {
    @Command
    public void doLogout() {
        AuthenticationService as = AuthenticationService.getInstance();
        as.logout();
        Executions.sendRedirect("/index.zul");
    }
}
