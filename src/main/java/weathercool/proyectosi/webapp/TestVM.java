package weathercool.proyectosi.webapp;

import static weathercool.proyectosi.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import weathercool.proyectosi.Employee;
import weathercool.proyectosi.TransactionUtils;

public class TestVM {

	private String name = "anonymous";
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getEmployeesCount() {
		return getDesktopEntityManager().createQuery("SELECT e FROM Employee e", Employee.class).getResultList().size();
	}
	
	@Command
	@NotifyChange("employeesCount")
	public void submitEmployee() {
		Employee emp = new Employee();
		emp.setName(name);
		
		TransactionUtils.doTransaction(
				getDesktopEntityManager(), 
				em->{ em.persist(emp);}
		);
	}
}
