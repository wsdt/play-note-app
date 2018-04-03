package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import models.Note;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.EbeanCategoryRepository;
import services.EbeanNoteRepository;

import javax.inject.Inject;
import java.util.List;

public class NoteController extends Controller {

    protected EbeanNoteRepository noteRepository;
    protected EbeanCategoryRepository categoryRepository;
    protected Form<Note> NoteForm;

    @Inject
    public NoteController(EbeanCategoryRepository categoryRepository, EbeanNoteRepository noteRepository, FormFactory formFactory) {
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
        this.NoteForm = formFactory.form(Note.class);
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
        noteRepository.saveNote(note);
        return ok(Json.toJson(note));
    }

    public Result delete(int id) {
        noteRepository.deleteNote(id);
        return ok();
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
