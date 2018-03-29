package services;

import io.ebean.Ebean;
import models.Category;
import models.Note;

import java.util.Arrays;
import java.util.List;

public class EbeanNoteRepository {

    public List<Note> getNotes() {
        return Ebean.find(Note.class).findList();
    }

    public Note getNote(int id) {
        return Ebean.find(Note.class)
                    .where()
                    .eq("id", id)
                    .findOne();
    }

    public void saveNote(Note note) {
        note.setLastEdited( (int) (System.currentTimeMillis() / 1000L) );

        if (note.getId() > 0) {
            Ebean.update(note);
        } else {
            Ebean.save(note);
        }
    }

    public void deleteNote(int id) {
        Ebean.delete(Note.class, id);
    }

    //CATEGORY --------------------------------------
    public List<Category> getAllCategories() {
        List<Category> categoryList = Ebean.find(Category.class).findList();
        if (categoryList.size() <= 0) {
            //add example categories
            Ebean.saveAll(Arrays.asList(
                    new Category("Holiday"),
                    new Category("Surfing"),
                    new Category("Fun")));
            categoryList = Ebean.find(Category.class).findList(); //do it again
        }
        return categoryList;
    }
}
