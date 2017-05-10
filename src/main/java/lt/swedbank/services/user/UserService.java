package lt.swedbank.services.user;

import lt.swedbank.beans.User;
import lt.swedbank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;



    /**
     *
     * Function returns a sure by an email
     *
     * @param email
     * @return
     */
    public User getUserByEmail(String email){
        if (!Optional.ofNullable(userRepository.findByEmail(email)).isPresent()) {
            //TODO throwinam, kad nera
        }

        return userRepository.findByEmail(email);
    }
}
