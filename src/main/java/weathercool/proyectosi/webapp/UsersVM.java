package weathercool.proyectosi.webapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import weathercool.proyectosi.TransactionUtils;
import weathercool.proyectosi.User;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import weathercool.proyectosi.webapp.utils.DesktopEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UsersVM {
    public List<User> getUsers() {
        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        List<User> users = em.createQuery("SELECT u FROM User u ORDER BY u.username", User.class).getResultList();
        return users;
    }

    @Command
    @NotifyChange("users")
    public void delete(@BindingParam("u") User user) {
        LogService.getInstance().logDelete("user", user.getUsername());

        EntityManager em = DesktopEntityManager.getDesktopEntityManager();
        TransactionUtils.doTransaction(em, __ -> {
            em.remove(user);
        });
    }

    private User editUser = null;
    public User getEditUser() { return this.editUser; }
    private User newUser = null;
    public User getNewUser() { return this.newUser; }

    @Command
    @NotifyChange("newUser")
    public void newUser() {
        this.newUser = new User();
    }

    @Command
    @NotifyChange("editUser")
    public void editUser(@BindingParam("u") User user) {
        this.editUser = user;
    }

    @Command
    @NotifyChange("newUser")
    public void cancelNewUser() {
        this.newUser = null;
    }

    @Command
    @NotifyChange("editUser")
    public void cancelEditUser() {
        this.editUser = null;
    }

    @Command
    @NotifyChange({"users", "newUser"})
    public void saveNewUser() {
        if (this.newUser != null) {
            EntityManager em = DesktopEntityManager.getDesktopEntityManager();
            TransactionUtils.doTransaction(em, __ -> {
                em.persist(this.newUser);
            });

            LogService.getInstance().logCreate("user");
            this.newUser = null;
        }
    }

    @Command
    @NotifyChange({"users", "editUser"})
    public void saveEditUser() {
        if (this.editUser != null) {
            EntityManager em = DesktopEntityManager.getDesktopEntityManager();
            TransactionUtils.doTransaction(em, __ -> {
                em.persist(this.editUser);
            });

            LogService.getInstance().logEdit("user", this.editUser.getUsername());
            this.editUser = null;
        }
    }
}
