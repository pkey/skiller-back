package lt.swedbank.exceptions.skillTemplate;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class NoSkillTemplateFoundException extends MainException{

    public NoSkillTemplateFoundException() {
        this.messageCode = "no_skill_template_found";
        this.statusCode = HttpStatus.NOT_FOUND;
    }
}
