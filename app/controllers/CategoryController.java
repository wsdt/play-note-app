package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import middlewares.SessionAuthenticationMiddleware;
import models.Category;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.EbeanCategoryRepository;

import javax.inject.Inject;
import java.util.List;

@With(SessionAuthenticationMiddleware.class)
public class CategoryController extends Controller {

    protected EbeanCategoryRepository categoryRepository;
    protected Form<Category> categoryForm;

    @Inject
    public CategoryController(EbeanCategoryRepository categoryRepository, FormFactory formFactory) {
        this.categoryRepository = categoryRepository;
        this.categoryForm = formFactory.form(Category.class);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result save() {
        JsonNode json = request().body().asJson();

        if (json.findPath("title").textValue() == null || json.findPath("title").textValue().isEmpty()) {
            return badRequest("Title is required.");
        }

        Category category = Json.fromJson(json, Category.class);
        categoryRepository.saveCategory(category);
        return ok(Json.toJson(category));
    }

    public Result delete(int id) {
        categoryRepository.deleteCategory(id);
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
        if (categoryRepository.getCategory(id) == null) {
            return badRequest("Category does not exist.");
        }

        Category category = Json.fromJson(json, Category.class);
        category.setId(id);
        categoryRepository.updateCategory(category);
        return ok();
    }

    public Result get(int id) {
        Category category = categoryRepository.getCategory(id);

        if (category == null) {
            return badRequest("Category does not exist.");
        } else {
            return ok(Json.toJson(category));
        }
    }

    public Result list() {
        String order = request().getQueryString("sort");
        if (order == null) {
            order = "asc"; //by default
        } else {
			if (!order.equals("asc") && !order.equals("desc")) {
				order = "asc";
			} 
		}

        List<Category> allCategories = categoryRepository.getCategories(order);
        String like = request().getQueryString("query");
        if (like != null) {
            if (!like.isEmpty()) {
                allCategories = categoryRepository.getCategoriesLike(order,like);
            }
        }

        return ok(Json.toJson(allCategories));
    }

}
