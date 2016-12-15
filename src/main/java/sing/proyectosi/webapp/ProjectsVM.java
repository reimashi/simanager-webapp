package sing.proyectosi.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import sing.proyectosi.Employee;
import sing.proyectosi.Project;
import sing.proyectosi.TransactionUtils;
import sing.proyectosi.webapp.util.DesktopEntityManagerManager;

public class ProjectsVM {

	// department under edition...
	private Project currentProject = null;
	
	public Project getCurrentProject() {
		return currentProject;
	}
	
	public List<Employee> getEmployees() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
	}
	
	public List<Project> getProjects() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT p FROM Project p", Project.class).getResultList();
	}
	
	@Command
	@NotifyChange("projects")
	public void delete(@BindingParam("p") Project project) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(project);
		});
	}
	
	@Command
	@NotifyChange("currentProject")
	public void newProject() {
		this.currentProject = new Project();
	}
	
	@Command
	@NotifyChange({"projects", "currentProject"})
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentProject);
		});
		this.currentProject = null;
	}
	
	@Command
	@NotifyChange("currentProject")
	public void cancel() {
		this.currentProject = null;
	}
	
	@Command
	@NotifyChange("currentProject")
	public void edit(@BindingParam("p") Project project) {
		this.currentProject = project;
	}
}
