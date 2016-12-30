package weathercool.proyectosi.webapp.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.DesktopCleanup;
import weathercool.proyectosi.User;

public class DesktopEntityManager {

    private static final String ENTITY_MANAGER_NAME = "__ENTITY_MANAGER__";
    private static EntityManagerFactory emf = null;

    public static EntityManager getDesktopEntityManager() {
        Desktop currentDesktop = Executions.getCurrent().getDesktop();

        if (currentDesktop != null) {
            if (currentDesktop.hasAttribute(ENTITY_MANAGER_NAME)) {
                return (EntityManager) currentDesktop.getAttribute(ENTITY_MANAGER_NAME);
            } else {

                EntityManager newEm = createNewEntityMamanger();
                currentDesktop.setAttribute(ENTITY_MANAGER_NAME, newEm);
                currentDesktop.addListener(new DesktopCleanup() {

                    @Override
                    public void cleanup(Desktop arg0) throws Exception {
                        newEm.close();
                    }
                });

                CreateAdminUser(newEm);

                return newEm;
            }

        } else {
            throw new IllegalArgumentException("Desktop not found in this execution");
        }
    }

    private static EntityManager createNewEntityMamanger() {
        EntityManagerFactory emf = getOrCreateEntityManagerFactory();
        return emf.createEntityManager();
    }

    private static EntityManagerFactory getOrCreateEntityManagerFactory() {
        if (emf != null) {
            return emf;
        }

        final String persistenceUnitName = Executions.getCurrent().getDesktop().getWebApp().getConfiguration()
                .getPreference("persistence-unit-name", "");
        if (persistenceUnitName.equals("")) {
            throw new IllegalArgumentException("no 'persistence-unit-name' preference found in zk.xml");
        } else {
            emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                emf.close();
            }
        });

        return emf;
    }

    private static void CreateAdminUser(EntityManager em) {
        User admin = null;

        try {
            admin = em.find(User.class, "admin");
        }
        catch(Exception e) {}

        if (admin == null) {
            try {
                admin = new User("admin");
                admin.setPassword("admin");
                admin.setName("Administrador");
                em.persist(admin);
            } catch (Exception e) {
            }
        }
    }
}