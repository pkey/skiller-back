package lt.swedbank.FakeAuthClasses;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.LoginUserRequest;
import lt.swedbank.beans.request.RegisterUserRequest;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class FakeAuthService implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(RegisterUserRequest registerUserRequest) throws Auth0Exception {
        String authId;
        if(userRepository.findByEmail(registerUserRequest.getEmail()) == null)
        {
            authId = registerUserRequest.getEmail();
        }
        else throw new UserAlreaduExistException();
        User user = new User(registerUserRequest);
        capitalizeUserNameAndLastName(user);
        user.setAuthId(authId);
        return userRepository.save(user);
    }

    private void capitalizeUserNameAndLastName(User user) {
        user.setName(user.getName().substring(0, 1).toUpperCase() + user.getName().substring(1));
        user.setLastName(user.getLastName().substring(0, 1).toUpperCase() + user.getLastName().substring(1));
    }

    @Override
    public TokenHolder loginUser(LoginUserRequest user) throws Auth0Exception {
        return null;
    }

    @Override
    public String extractAuthIdFromToken(String token) {
        return token.substring(7);
    }


    public FakeTokenHolder loginFakeUser(LoginUserRequest loginUserRequest) throws Auth0Exception {
        User user;
        if(userRepository.findByEmail(loginUserRequest.getEmail()) != null)
        {
            user = userRepository.findByEmail(loginUserRequest.getEmail());
        }
        else {
            throw new UserNotFoundException();
        }
        return new FakeTokenHolder(user.getAuthId());
    }
}
