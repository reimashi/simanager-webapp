package weathercool.proyectosi.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import weathercool.proyectosi.Alert;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

public class AlertsVM {


    private Alert currentAlert=null;

    public Alert getCurrentAlert() {
        return currentAlert;
    }

    public List<Alert> getAlerts() {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        return em.createQuery("SELECT a FROM Alert a", Alert.class).getResultList();
    }

    @Command
    @NotifyChange("alert")
    public void delete(@BindingParam("a") Alert alert) {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.remove(alert);
        });
    }

    @Command
    @NotifyChange("currentAlert")
    public void newAlert() {
        this.currentAlert = new Alert();
    }

    @Command
    @NotifyChange("currentAlert")
    public void cancel() {
        this.currentAlert = null;
    }

    @Command
    @NotifyChange("currentAlert")
    public void edit(@BindingParam("a") Alert alert) {
        this.currentAlert = alert;
    }

    @Command
    @NotifyChange({"alerts", "currentAlert"})
    public void save() {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.persist(this.currentAlert);
        });
        this.currentAlert = null;
    }
}
