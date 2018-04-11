package middlewares;

import models.User;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.EbeanUserRepository;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SessionAuthenticationMiddleware extends Action.Simple {
    private static final String TAG = "SessionAuthMiddleware";
    @Inject
    protected EbeanUserRepository userRepository;

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        Logger.info(TAG + ":call: " + ctx.request().method() + ": " + ctx.request().path());

        if (getUserFromCurrSess() == null) {
            return CompletableFuture.completedFuture(redirect("/auth"));
        } else {
            return delegate.call(ctx);
        }
    }

    public User getUserFromCurrSess() {
        String sessUsername = Http.Context.current().session().get("username");
        User currUser = userRepository.getUserByUsername(sessUsername);

        if (currUser == null || sessUsername == null) {
            return null;
        } else {
            return currUser;
        }
    }

    public void setCurrSessFromUser(@Nonnull User authenticatedUser) {
        Http.Context.current().session().put("username",authenticatedUser.getUsername());
    }

    public void deleteCurrSession() {
        Http.Context.current().session().clear();//clear all (otherwise just remove)
    }
}
