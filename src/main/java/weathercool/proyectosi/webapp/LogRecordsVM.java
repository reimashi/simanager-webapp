package weathercool.proyectosi.webapp;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import weathercool.proyectosi.LogRecord;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.User;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

import javax.persistence.EntityManager;
import java.util.List;

public class LogRecordsVM {
    public List<LogRecord> getLogs() {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        List<LogRecord> logs = em.createQuery("SELECT u FROM LogRecord u ORDER BY u.time", LogRecord.class).getResultList();
        return logs;
    }

    @Command
    @NotifyChange("logs")
    public void delete(@BindingParam("u") LogRecord record) {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.remove(record);
        });
    }
}
