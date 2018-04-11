package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import middlewares.SessionAuthenticationMiddleware;
import models.Note;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import services.EbeanCategoryRepository;
import services.EbeanNoteRepository;
import services.EbeanUserRepository;

import javax.inject.Inject;
import java.util.List;

@With(SessionAuthenticationMiddleware.class)
public class NoteController extends Controller {

    protected SessionAuthenticationMiddleware sessionAuthenticationMiddleware;
    protected EbeanNoteRepository noteRepository;
    protected EbeanCategoryRepository categoryRepository;
    protected EbeanUserRepository userRepository;
    protected Form<Note> NoteForm;

    @Inject
    public NoteController(SessionAuthenticationMiddleware sessionAuthenticationMiddleware, EbeanCategoryRepository categoryRepository, EbeanNoteRepository noteRepository, FormFactory formFactory, EbeanUserRepository userRepository) {
        this.sessionAuthenticationMiddleware = sessionAuthenticationMiddleware;
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
        this.NoteForm = formFactory.form(Note.class);
        this.userRepository = userRepository;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result save() {
        JsonNode json = request().body().asJson();

        if (json.findPath("title").textValue() == null || json.findPath("title").textValue().isEmpty()) {
            return badRequest("Title is required.");
        }

        int categoryId = json.get("categoryId").asInt();

        Note note = Json.fromJson(json, Note.class);
        note.setCategory(categoryRepository.getCategory(categoryId));
        User tmpUser = userRepository.getUserByUsername(Http.Context.current().session().get("username"));
        System.out.println("Fuckin user: " + tmpUser);
        note.setUser(tmpUser);
        noteRepository.saveNote(note);
        return ok(Json.toJson(note));
    }

    public Result delete(int id) {
        if (sessionAuthenticationMiddleware.getUserFromCurrSess().getUsername().equals(noteRepository.getNote(id).getUser().getUsername())) {
            noteRepository.deleteNote(id);
            return ok();
        } else {
            return forbidden("Admin has no permission to delete notes of other users!");
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result update(int id) {
        JsonNode json = request().body().asJson();

        if (json == null) {
            return badRequest("No parseable json found.");
        }
        if (json.findPath("title").textValue() == null || json.findPath("title").textValue().isEmpty()) {
            return badRequest("Title is required.");
        }
        if (noteRepository.getNote(id) == null) {
            return badRequest("Note does not exist.");
        }

        int categoryId = json.get("categoryId").asInt();

        Note note = Json.fromJson(json, Note.class);
        note.setCategory(categoryRepository.getCategory(categoryId));
        //user setzen nicht notwendig und nicht gut (wenn admin z.b. fremde notizen ändert)
        note.setId(id);
        noteRepository.saveNote(note);
        return ok();
    }

    public Result get(int id) {
        Note Note = noteRepository.getNote(id);

        if (Note == null) {
            return badRequest("Note does not exist.");
        } else {
            return ok(Json.toJson(Note));
        }
    }

    public Result list() {
        List<Note> allNotes = noteRepository.getNotes();
        return ok(Json.toJson(allNotes));
    }
}
