package models;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 10.04.2018
 * Time: 13:42
 */

@Entity
public class User {
    @Id
    private int id;

    @Column(unique=true)
    private String username;
    private String password;
    private boolean isAdmin = false; //by default false

    @OneToMany
    protected List<Note> noteList;

    public static String getHash(@Nonnull String input) {
        try {
            return new String(MessageDigest.getInstance("SHA-512").digest(input.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return input;
    }

    public boolean comparePasswords(@Nonnull String clearTextPwd) {
        return this.getPassword().equals(getHash(clearTextPwd));
    }

    public void setPasswordInCleartext(String password) {
        this.setPassword(User.getHash(password));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public UserChangePwdForm getUserChangePwdForm() {
        return new UserChangePwdForm();
    }
}
