package weathercool.proyectosi.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import weathercool.proyectosi.Location;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

public class LocationVM {

	private Location currentLocation = null;
	
	public Location getCurrentLocation() {
		return currentLocation;
	}
	
	public List<Location> getLocations() {
		EntityManager em = DesktopEntityManager.getDesktopEntityManager();
		return em.createQuery("SELECT l FROM location l", Location.class).getResultList();
	}
	
	@Command
	@NotifyChange("location")
	public void delete(@BindingParam("l") Location location) {
		EntityManager em = DesktopEntityManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(location);
		});
	}
	
	@Command
	@NotifyChange("currentLocation")
	public void newLocation() {
		this.currentLocation = new Location();
	}
	
	@Command
	@NotifyChange({"locations", "currentLocation"})
	public void save() {
		EntityManager em = DesktopEntityManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentLocation);
		});
		this.currentLocation = null;
	}
	
	@Command
	@NotifyChange("currentLocation")
	public void cancel() {
		this.currentLocation = null;
	}
	
	@Command
	@NotifyChange("currentLocation")
	public void edit(@BindingParam("l") Location location) {
		this.currentLocation = location;
	}
}
