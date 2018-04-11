package controllers;

import middlewares.BasicAuthenticationMiddleware;
import middlewares.SessionAuthenticationMiddleware;
import models.Note;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import services.EbeanCategoryRepository;
import services.EbeanNoteRepository;
import services.EbeanUserRepository;

import javax.inject.Inject;
import java.util.List;

@With(SessionAuthenticationMiddleware.class)
public class HomeController extends Controller {

    @Inject
    protected SessionAuthenticationMiddleware sessionAuthenticationMiddleware;
    @Inject
    protected EbeanNoteRepository noteRepository;
    @Inject
    protected EbeanCategoryRepository categoryRepository;
    @Inject
    protected EbeanUserRepository userRepository;

    protected Form<Note> noteForm;

    @Inject
    public HomeController(FormFactory formFactory) {
        this.noteForm = formFactory.form(Note.class);
    }

    public Result index() {
        List<Note> notes = noteRepository.getNotes();

        return ok(views.html.index.render(notes));
    }

    public Result form(int id) {
        Note note = new Note();

        if (id > 0) {
            note = noteRepository.getNote(id);
        }

        return ok(views.html.form.render(noteForm.fill(note),categoryRepository.getCategories("asc")));
    }

    public Result save() {
        Form<Note> form = noteForm.bindFromRequest();

        if(form.hasErrors()){
            return badRequest(views.html.form.render(form,categoryRepository.getCategories("asc")));
        }else{
            Note tmpNote = form.get();
            tmpNote.setUser(userRepository.getUserByUsername(Http.Context.current().session().get("username")));
            noteRepository.saveNote(form.get());
            flash("success","This note was successfully saved.");
            return redirect("/");
        }
    }

    public Result delete(int id) {
        if (sessionAuthenticationMiddleware.getUserFromCurrSess().getUsername().equals(noteRepository.getNote(id).getUser().getUsername())) {
            noteRepository.deleteNote(id);
            return ok();
        } else {
            return forbidden("Admin has no permission to delete notes of other users.");
        }
    }

}