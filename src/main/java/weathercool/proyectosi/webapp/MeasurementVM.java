package weathercool.proyectosi.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import weathercool.proyectosi.Alert;
import weathercool.proyectosi.LocationClass;
import weathercool.proyectosi.Measurement;
import weathercool.proyectosi.Time;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

public class MeasurementVM {
    private Measurement currentMeasurement=null;

    public Measurement getCurrentMeasurement() {
        return currentMeasurement;
    }

    public List<Measurement> getMeasurements() {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        return em.createQuery("SELECT a FROM Measurement a", Measurement.class).getResultList();
    }

    public List<Alert> getAlert() {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        return em.createQuery("SELECT d FROM Alert d", Alert.class).getResultList();
    }
    public List<Time> getTime() {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        return em.createQuery("SELECT d FROM Time d", Time.class).getResultList();
    }

    public List<LocationClass> getLocation() {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        return em.createQuery("SELECT d FROM LocationClass d", LocationClass.class).getResultList();
    }

    @Command
    @NotifyChange("currentMeasurement")
    public void newMeasurement() {
        this.currentMeasurement = new Measurement();

        LogService.getInstance().logCreate("measurement");
    }

    @Command
    @NotifyChange("currentMeasurement")
    public void cancel() {
        this.currentMeasurement = null;
    }

    @Command
    @NotifyChange("currentMeasurement")
    public void edit(@BindingParam("m") Measurement measurement) {
        this.currentMeasurement = measurement;
    }

    @Command
    @NotifyChange("measurement")
    public void delete(@BindingParam("m") Measurement measurement) {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.remove(measurement);
        });

        LogService.getInstance().logDelete("measurement");
    }

    @Command
    @NotifyChange({"measurements", "currentMeasurement"})
    public void save() {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.persist(this.currentMeasurement);
        });

        LogService.getInstance().logEdit("measurement");
        this.currentMeasurement = null;
    }

}
