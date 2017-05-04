package lt.swedbank.services.user;

import lt.swedbank.beans.User;

/**
 * Created by paulius on 5/4/17.
 */
public interface IUserService {
    User getUserByEmail(String email);
}
