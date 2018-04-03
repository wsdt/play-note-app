package services;

import io.ebean.Ebean;
import models.Note;

import javax.annotation.Nonnull;
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
        note.setLastEdited( (int) (System.currentTimeMillis() / 1000L));

        if (Ebean.find(Note.class,note.getId()) == null) {
            Ebean.save(note);
        } else {
            Ebean.update(note);
        }
    }

    public void deleteNote(int id) {
        Ebean.delete(Note.class, id);
    }

}
