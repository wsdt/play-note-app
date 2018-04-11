package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import middlewares.SessionAuthenticationMiddleware;
import models.Note;
import models.User;
import models.UserChangePwdForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import services.EbeanCategoryRepository;
import services.EbeanNoteRepository;
import services.EbeanUserRepository;

import javax.inject.Inject;
import java.util.List;

//HERE NO MIDDLEWARE
public class AuthController extends Controller {
    protected SessionAuthenticationMiddleware sessionAuthenticationMiddleware;
    protected EbeanUserRepository ebeanUserRepository;
    protected Form<User> userForm;
    protected Form<UserChangePwdForm> userChangePwdFormForm;

    @Inject
    public AuthController(SessionAuthenticationMiddleware sessionAuthenticationMiddleware, EbeanUserRepository ebeanUserRepository, FormFactory formFactory) {
        this.ebeanUserRepository = ebeanUserRepository;
        this.sessionAuthenticationMiddleware = sessionAuthenticationMiddleware;
        this.userForm = formFactory.form(User.class);
        this.userChangePwdFormForm = formFactory.form(UserChangePwdForm.class);
    }

    public Result sessAuth() {
        User currUser = sessionAuthenticationMiddleware.getUserFromCurrSess();
        if (currUser == null) {
            //is session not available determine whether form was submitted
            return ok(views.html.loginform.render());
        }
        return redirect("/");
    }

    private static boolean isTextInvalid(String input) {
        return (input == null) || (input.isEmpty());
    }


    public Result dbAuth() {
        Form<User> form = userForm.bindFromRequest();
        User user = form.get(); //attention pwd is saved in cleartext!

        /*if (isTextInvalid(json.findPath("username").textValue()) || isTextInvalid(json.findPath("password").textValue())) {
            return badRequest("Title is required.");
        }*/

        if (user != null) {
            User desiredUser = ebeanUserRepository.getUserByUsername(user.getUsername()); //todo: escape in ebeanrep but i dont care

            if (desiredUser != null && desiredUser.comparePasswords(user.getPassword())) {
                //now set session
                sessionAuthenticationMiddleware.setCurrSessFromUser(desiredUser);
                System.out.println("worked");
            }
            System.out.println("User not null.");
        }

        return redirect("/");
    }

    public Result logout() {
        sessionAuthenticationMiddleware.deleteCurrSession();
        return redirect("/auth");
    }

    public Result showChangePwdForm() {
        return ok(views.html.changepwdform.render());
    }

    public Result changePwd() {
        Form<UserChangePwdForm> form = userChangePwdFormForm.bindFromRequest();
        UserChangePwdForm userChangePwdForm = form.get();
        User currUser = sessionAuthenticationMiddleware.getUserFromCurrSess();

        if (userChangePwdForm != null) {
            System.out.println("pwdform: "+userChangePwdForm.getOldPassword()+":"+userChangePwdForm.getNewPassword()+":"+userChangePwdForm.getNewPasswordRefresh());
            if (currUser.comparePasswords(userChangePwdForm.getOldPassword())) {
                //old pwd corr
                if (userChangePwdForm.getNewPassword().equals(userChangePwdForm.getNewPasswordRefresh())) {
                    //new password correct retyped
                    currUser.setPasswordInCleartext(userChangePwdForm.getNewPassword());
                    ebeanUserRepository.save(currUser);
                    //successfully saved
                    flash("success","Password successfully changed.");
                    return redirect("/");
                }
            }
        }
        //here we could also add flash("error", etc.);
        return forbidden("You are not allowed to change pwd.");

        //return ok();
    }
}