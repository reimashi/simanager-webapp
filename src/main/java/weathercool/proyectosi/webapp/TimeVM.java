package weathercool.proyectosi.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import weathercool.proyectosi.Time;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.webapp.util.DesktopEntityManagerManager;

public class TimeVM {

	private Time currentTime = null;
	
	public Time getCurrentTime() {
		return currentTime;
	}
	
	public List<Time> getTimes() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT t FROM time t", Time.class).getResultList();
	}
	
	@Command
	@NotifyChange("time")
	public void delete(@BindingParam("t") Time time) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(time);
		});
	}
	
	@Command
	@NotifyChange("currentTime")
	public void newTime() {
		this.currentTime = new Time();
	}
	
	@Command
	@NotifyChange({"times", "currentTime"})
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentTime);
		});
		this.currentTime = null;
	}
	
	@Command
	@NotifyChange("currentTime")
	public void cancel() {
		this.currentTime = null;
	}
	
	@Command
	@NotifyChange("currentTime")
	public void edit(@BindingParam("l") Time time) {
		this.currentTime = time;
	}
}
