package services;

import io.ebean.Ebean;
import models.User;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 10.04.2018
 * Time: 13:51
 */
public class EbeanUserRepository {

    public EbeanUserRepository() {
        if (Ebean.find(User.class).findCount() <= 0) {
            User user1 = new User();
            user1.setUsername("zwerg1");
            user1.setPasswordInCleartext("festung");
            Ebean.save(user1);

            User user2 = new User();
            user2.setUsername("zwerg2");
            user2.setPasswordInCleartext("festung");
            Ebean.save(user2);
        }
    }

    public User getUserByUsername(String username) {
        return Ebean.find(User.class)
                .where().eq("username",username)
                .findOne();
    }
}
