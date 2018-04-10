package middlewares;

import models.User;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.EbeanUserRepository;

import javax.inject.Inject;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class BasicAuthenticationMiddleware extends Action.Simple {
    private static final String TAG = "BasicAuthMiddleware";
    @Inject
    protected EbeanUserRepository userRepository;

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        Logger.info(TAG + ":call: " + ctx.request().method() + ": " + ctx.request().path());

        Optional<String> header = ctx.request().header("Authorization");

        if (header.isPresent() && header.get().startsWith("Basic ")) {
            String[] auth = new String(Base64.getDecoder().decode(header.get().substring(6))).split(":"); //byte[] zu Str
            if (auth.length == 2) { 
                User user = userRepository.getUserByUsername(auth[0]);

                if (user != null && user.comparePasswords(auth[1])) { //with & nullpointer would occur, with && it will abort before next validation :)
                    return delegate.call(ctx);
                }
            }
        }
        Result result = unauthorized("Authentication required.").withHeader("WWW-Authenticate", "Basic realm=\"Secure Area\"");
        return CompletableFuture.completedFuture(result);
    }
}
