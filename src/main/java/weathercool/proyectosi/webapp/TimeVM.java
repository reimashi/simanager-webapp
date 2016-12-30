package weathercool.proyectosi.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import weathercool.proyectosi.Time;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

public class TimeVM {

	private Time currentTime = null;
	
	public Time getCurrentTime() {
		return currentTime;
	}
	
	public List<Time> getTimes() {
		EntityManager em = DesktopEntityManager.getDesktopEntityManager();
		return em.createQuery("SELECT t FROM Time t", Time.class).getResultList();
	}
	
	@Command
	@NotifyChange("time")
	public void delete(@BindingParam("t") Time time) {
		LogService.getInstance().logDelete("time", String.valueOf(time.getId()));

		EntityManager em = DesktopEntityManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(time);
		});

		LogService.getInstance().logDelete("time", String.valueOf(this.currentTime.getId()));
	}
	
	@Command
	@NotifyChange("currentTime")
	public void newTime() {
		this.currentTime = new Time();

		LogService.getInstance().logCreate("time");
	}
	
	@Command
	@NotifyChange({"times", "currentTime"})
	public void save() {
		EntityManager em = DesktopEntityManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentTime);
		});

		LogService.getInstance().logEdit("time", String.valueOf(this.currentTime.getId()));
		this.currentTime = null;
	}
	
	@Command
	@NotifyChange("currentTime")
	public void cancel() {
		this.currentTime = null;
	}
	
	@Command
	@NotifyChange("currentTime")
	public void edit(@BindingParam("t") Time time) {
		this.currentTime = time;
	}
}
