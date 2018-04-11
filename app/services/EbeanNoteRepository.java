package services;

import io.ebean.Ebean;
import middlewares.SessionAuthenticationMiddleware;
import models.Note;
import models.User;
import play.mvc.Http;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

public class EbeanNoteRepository {
    @Inject
    protected SessionAuthenticationMiddleware sessionAuthenticationMiddleware;

    public List<Note> getNotes() {
        User currUser = sessionAuthenticationMiddleware.getUserFromCurrSess();

        if (currUser.isAdmin()) {
            return Ebean.find(Note.class).findList(); //show all
        } else {
            return Ebean.find(Note.class).where().
                    eq("user", currUser).findList(); //just return only user related
        }
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
