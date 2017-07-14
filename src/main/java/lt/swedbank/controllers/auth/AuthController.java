package lt.swedbank.controllers.auth;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.LoginUserRequest;
import lt.swedbank.beans.request.RegisterUserRequest;
import lt.swedbank.beans.response.LoginTokenResponse;
import lt.swedbank.beans.response.RegisterUserResponse;
import lt.swedbank.services.auth.Auth0AuthenticationService;
import lt.swedbank.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Component
@CrossOrigin(origins = "*")
public class AuthController {

    private AuthenticationService authService;

    @Autowired
    public void setAuthenticationService(Auth0AuthenticationService authService) {
        this.authService = authService;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    LoginTokenResponse login(@Valid @RequestBody LoginUserRequest user) throws Auth0Exception {
        TokenHolder token = authService.loginUser(user);
        return new LoginTokenResponse(token);
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody
    RegisterUserResponse register(@Valid @RequestBody RegisterUserRequest user) throws Auth0Exception {
        User registeredUser = authService.registerUser(user);
        return new RegisterUserResponse(registeredUser);
    }

}
