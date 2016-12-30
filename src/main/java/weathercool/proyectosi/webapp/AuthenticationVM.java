package weathercool.proyectosi.webapp;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import weathercool.proyectosi.User;

import javax.persistence.EntityManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationVM extends SelectorComposer<Component> {
    private static Logger logger = Logger.getLogger(AuthenticationVM.class.getName());

    @Wire
    Textbox accountInput;
    @Wire
    Textbox passwordInput;
    @Wire
    Label message;

    //services
    AuthenticationService authService = AuthenticationService.getInstance();

    @Listen("onClick=#login; onOK=#loginWin")
    public void doLogin(){
        String nm = accountInput.getValue();
        String pd = passwordInput.getValue();

        logger.log(Level.INFO, "Formulario de login - Usuario: " + nm + " - Contraseña: " + pd);

        if(!authService.login(nm,pd)){
            message.setValue("El usuario o la contraseña son incorrectos.");
            return;
        }
        else {
            User cre = authService.getUserInfo();
            message.setValue("Bienvenido, " + cre.getName());
            message.setSclass("");

            Executions.sendRedirect("/index.zul");
        }
    }
}
