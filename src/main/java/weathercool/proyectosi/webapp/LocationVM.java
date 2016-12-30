package weathercool.proyectosi.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import weathercool.proyectosi.LocationClass;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

public class LocationVM {

	private LocationClass currentLocation = null;
	
	public LocationClass getCurrentLocation() {
		return currentLocation;
	}
	
	public List<LocationClass> getLocations() {
		EntityManager em = DesktopEntityManager.getDesktopEntityManager();
		return em.createQuery("SELECT l FROM LocationClass l", LocationClass.class).getResultList();
	}
	
	@Command
	@NotifyChange("location")
	public void delete(@BindingParam("l") LocationClass location) {
		LogService.getInstance().logDelete("location", String.valueOf(location.getId()));

		EntityManager em = DesktopEntityManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(location);
		});
	}
	
	@Command
	@NotifyChange("currentLocation")
	public void newLocation() {
		this.currentLocation = new LocationClass();

		LogService.getInstance().logCreate("location");
	}
	
	@Command
	@NotifyChange({"locations", "currentLocation"})
	public void save() {
		EntityManager em = DesktopEntityManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentLocation);
		});

		LogService.getInstance().logEdit("location", String.valueOf(this.currentLocation.getId()));
		this.currentLocation = null;
	}
	
	@Command
	@NotifyChange("currentLocation")
	public void cancel() {
		this.currentLocation = null;
	}
	
	@Command
	@NotifyChange("currentLocation")
	public void edit(@BindingParam("l") LocationClass location) {
		this.currentLocation = location;
	}
}
