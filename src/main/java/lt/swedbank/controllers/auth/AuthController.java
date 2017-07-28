package lt.swedbank.controllers.auth;

import com.auth0.exception.Auth0Exception;
import lt.swedbank.FakeAuthClasses.FakeAuthService;
import lt.swedbank.FakeAuthClasses.FakeTokenHolder;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.LoginUserRequest;
import lt.swedbank.beans.request.RegisterUserRequest;
import lt.swedbank.beans.response.LoginTokenResponse;
import lt.swedbank.beans.response.RegisterUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Component
@CrossOrigin(origins = "*")
public class AuthController {

    private FakeAuthService authService;



    // changed to fake
    @Autowired
    public void setAuthenticationService(FakeAuthService authService) {
        this.authService = authService;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    LoginTokenResponse login(@Valid @RequestBody LoginUserRequest user) throws Auth0Exception {
        FakeTokenHolder token = authService.loginFakeUser(user);
        return new LoginTokenResponse(token);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody
    RegisterUserResponse register(@Valid @RequestBody RegisterUserRequest user) throws Auth0Exception {
        User registeredUser = authService.registerUser(user);
        return new RegisterUserResponse(registeredUser);
    }

}
