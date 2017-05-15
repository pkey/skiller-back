package lt.swedbank.interceptors.auth;

import lt.swedbank.services.auth.Auth0AuthenticationService;
import lt.swedbank.services.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {


    private AuthenticationService authService;

    @Autowired
    public void setAuthenticationService(Auth0AuthenticationService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        String email = getRequestingUsersEmail(token);
        request.setAttribute("email", email);
        return true;
    }


    private String getRequestingUsersEmail(String token) throws Exception {

        return authService.getUser(token).getEmail();
    }
}
