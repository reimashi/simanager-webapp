package weathercool.proyectosi.webapp;

import weathercool.proyectosi.LogRecord;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.User;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

import javax.persistence.EntityManager;
import java.util.Date;

public class LogService {
    private static LogService instance = null;
    protected LogService() {}

    public static LogService getInstance() {
        if(instance == null) {
            instance = new LogService();
        }
        return instance;
    }

    public void log(String action, String table, String id) {
        AuthenticationService authSrv = AuthenticationService.getInstance();
        User user = null;

        LogRecord record = new LogRecord();

        record.setAction(action);
        record.setTableName(table);

        if (authSrv.isLoguedIn()) {
            record.setUser(authSrv.getUserInfo());
        }

        record.setRaw((id != null && id.length() > 0 ? "ID: " + id.toString() + ". " : "") + "Entry created by web LogService.");
        record.setTime(new Date());

        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.persist(record);
        });
    }

    public void logCreate(String table) { log("create", table, null); }
    public void logEdit(String table) { log("edit", table, null); }
    public void logDelete(String table) { log("remove", table, null); }

    public void logCreate(String table, String id) { log("create", table, id); }
    public void logEdit(String table, String id) { log("edit", table, id); }
    public void logDelete(String table, String id) { log("remove", table, id); }
}
