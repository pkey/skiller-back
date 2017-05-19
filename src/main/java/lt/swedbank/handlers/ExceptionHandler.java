package lt.swedbank.handlers;

import lt.swedbank.exceptions.ApplicationException;
import lt.swedbank.exceptions.ExceptionMessage;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.exceptions.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class ExceptionHandler {

    @Autowired
    private MessageSource messageSource;


    private String getMessage(String message) {
        return messageSource.getMessage(message, null, Locale.getDefault());
    }

    private Locale getLocale(HttpServletRequest request) {
        return new SessionLocaleResolver().resolveLocale(request);
    }

    public ApplicationException handleException(ExceptionMessage message) {

        switch (message){
            case USER_NOT_FOUND:
                return new UserNotFoundException(getMessage(message.getMsg()));
            case SKILL_ALREADY_EXISTS:
                return new SkillAlreadyExistsException(getMessage(message.getMsg()));
            case SKILL_NOT_FOUND:
                return new SkillNotFoundException(getMessage(message.getMsg()));
            default:
                return null;
        }
    }

}
