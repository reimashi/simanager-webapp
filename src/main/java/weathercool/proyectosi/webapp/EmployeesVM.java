package weathercool.proyectosi.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import weathercool.proyectosi.Department;
import weathercool.proyectosi.Employee;
import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.webapp.util.DesktopEntityManagerManager;

public class EmployeesVM {

	// Employee under edition...
	private Employee currentEmployee = null;
	
	public Employee getCurrentEmployee() {
		return currentEmployee;
	}
	
	public List<Department> getDepartments() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
	}
	public List<Employee> getEmployees() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
	}
	
	@Command
	@NotifyChange("employees")
	public void delete(@BindingParam("e") Employee employee) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(employee);
		});
	}
	
	@Command
	@NotifyChange("currentEmployee")
	public void newEmployee() {
		this.currentEmployee = new Employee();
	}
	
	@Command
	@NotifyChange({"employees", "currentEmployee"})
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentEmployee);
		});
		this.currentEmployee = null;
	}
	
	@Command
	@NotifyChange("currentEmployee")
	public void cancel() {
		this.currentEmployee = null;
	}
	
	@Command
	@NotifyChange("currentEmployee")
	public void edit(@BindingParam("e") Employee employee) {
		this.currentEmployee = employee;
	}
}
