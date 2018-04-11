package models;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 11.04.2018
 * Time: 07:26
 */
@Entity
public class UserChangePwdForm {
    protected String oldPassword;
    protected String newPassword;
    protected String newPasswordRefresh;


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRefresh() {
        return newPasswordRefresh;
    }

    public void setNewPasswordRefresh(String newPasswordRefresh) {
        this.newPasswordRefresh = newPasswordRefresh;
    }
}
