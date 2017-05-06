package lt.swedbank.services.user;

import lt.swedbank.beans.User;

public interface IUserService {
    User getUserByEmail(String email);
}
