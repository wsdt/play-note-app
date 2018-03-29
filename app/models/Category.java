package models;

import play.data.validation.Constraints;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Category {
    @Id
    @Constraints.Required(message = "Please add a title")
    @Constraints.MaxLength(value = 20, message = "Only 20 characters are allowed")
    protected String title = "";

    @OneToMany(cascade = CascadeType.ALL)
    public List<Note> noteList = new ArrayList<>();

    public Category() {}
    public Category(@Nonnull String title) {
        this.setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

