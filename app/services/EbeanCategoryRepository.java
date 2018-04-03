package services;

import io.ebean.Ebean;
import models.Category;

import javax.annotation.Nonnull;
import java.util.List;

public class EbeanCategoryRepository {

    public EbeanCategoryRepository() {
        if (Ebean.find(Category.class).findCount() <= 0) {
            Category category1 = new Category();
            category1.setTitle("Important");
            Ebean.save(category1);

            Category category2 = new Category();
            category2.setTitle("Shopping");
            Ebean.save(category2);

            Category category3 = new Category();
            category3.setTitle("Holiday");
            Ebean.save(category3);
        }
    }

    public void saveCategory(@Nonnull Category category) {
        Ebean.save(category);
    }

    public void updateCategory(@Nonnull Category category) {
        Ebean.update(category);
    }

    public List<Category> getCategories(@Nonnull String order) {
        return Ebean.find(Category.class).orderBy("title " + order).findList();
    }

    public List<Category> getCategoriesLike(@Nonnull String order, @Nonnull String likeQuery) {
        return Ebean.find(Category.class).orderBy("title " + order).where().like("title", "%" + likeQuery + "%").findList();
    }

    public Category getCategory(int id) {
        return Ebean.find(Category.class, id);
    }

    public void deleteCategory(@Nonnull Category category) {
        Ebean.delete(category);
    }

    public void deleteCategory(int id) {
        Ebean.delete(Category.class, id);
    }

}
