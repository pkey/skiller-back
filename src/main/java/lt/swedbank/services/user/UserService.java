package lt.swedbank.services.user;

import lt.swedbank.beans.User;
import lt.swedbank.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Created by paulius on 5/4/17.
 */
@Service
public class UserService implements IUserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     * Function returns a sure by an email
     *
     * @param email
     * @return
     */
    public User getUserByEmail(String email){
        User user = this.userRepository.findByEmail(email);

        return user;
    }
}
